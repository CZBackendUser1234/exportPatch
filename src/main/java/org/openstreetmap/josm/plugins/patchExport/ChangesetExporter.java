package org.openstreetmap.josm.plugins.patchExport;

import org.openstreetmap.josm.actions.ExtensionFileFilter;
import org.openstreetmap.josm.data.osm.*;
import org.openstreetmap.josm.gui.io.importexport.OsmExporter;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.tools.Logging;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.openstreetmap.josm.tools.I18n.tr;

/**
 *
 * Save patch to the selected file path
 * @author CZBackendUser1234
 *
 */
public class ChangesetExporter extends OsmExporter {

    private static final String EXPORT_EXTENSION = "osc";

    public ChangesetExporter() {
        super(new ExtensionFileFilter(EXPORT_EXTENSION, EXPORT_EXTENSION,
                tr("OSM Changeset Export") + " (*." + EXPORT_EXTENSION + ")"));
    }

    @Override
    public void doSave(File file, OsmDataLayer layer) {
        try {

            List<OsmPrimitive> deletedPrimitives = new ArrayList<>();
            List<OsmPrimitive> createPrimitives = new ArrayList<>();
            List<OsmPrimitive> modifyPrimitives = new ArrayList<>();

            // Проход по всем слоям
            Logging.info("Tracking changes on layer: " + layer.getName());
            for (OsmPrimitive primitive : layer.getDataSet().allPrimitives()) {
                if (primitive.isDeleted()) {
                    deletedPrimitives.add(primitive);
                    Logging.debug("delete:" + primitive);
                } else if (primitive.isNew()) {
                    createPrimitives.add(primitive);
                    Logging.debug("create:" + primitive);
                } else if (primitive.isModified()) {
                    modifyPrimitives.add(primitive);
                    Logging.debug("modify:" + primitive);
                }
            }

            String path = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('/'));

            String name = file.getName();

            String finalPath = saveToOscFile(createPrimitives, modifyPrimitives, deletedPrimitives, path, name);

            Logging.info("Changeset exported to " + finalPath);
        } catch (Exception e) {
            Logging.error("Error exporting changeset: " + e.getMessage());
        }
    }

    public String saveToOscFile(
            List<OsmPrimitive> created,
            List<OsmPrimitive> modified,
            List<OsmPrimitive> deleted,
            String path,
            String name
    ) throws Exception {
        String filePath = path + name + ".osc";
        File file = new File(filePath);

        try (Writer writer = new FileWriter(file)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<osmChange version=\"0.6\" generator=\"OsmChangeFileWriter\">\n");

            writeChangeSet(writer, "create", created);
            writeChangeSet(writer, "modify", modified);
            writeChangeSet(writer, "delete", deleted);

            writer.write("</osmChange>\n");
        }

        return filePath;
    }

    private static void writeChangeSet(Writer writer, String action, List<OsmPrimitive> primitives) throws Exception {
        if (!primitives.isEmpty()) {
            writer.write("  <" + action + ">\n");
            for (OsmPrimitive primitive : primitives) {
                writePrimitive(writer, primitive);
            }
            writer.write("  </" + action + ">\n");
        }
    }

    private static void writePrimitive(Writer writer, OsmPrimitive primitive) throws Exception {
        if (primitive instanceof Node) {
            Node node = (Node) primitive;
            writer.write(String.format("    <node id=\"%d\" version=\"%d\" timestamp=\"%s\" lat=\"%f\" lon=\"%f\">\n", node.getUniqueId(), node.getVersion(), node.getInstant().toString(), node.getCoor().lat(), node.getCoor().lon()));
            writeTags(writer, node);
            writer.write("    </node>\n");
        } else if (primitive instanceof Way) {
            Way way = (Way) primitive;
            writer.write(String.format("    <way id=\"%d\" version=\"%d\" timestamp=\"%s\">\n", way.getUniqueId(), way.getVersion(), way.getInstant().toString()));
            for (Node node : way.getNodes()) {
                writer.write(String.format("      <nd ref=\"%d\"/>\n", node.getUniqueId()));
            }
            writeTags(writer, way);
            writer.write("    </way>\n");
        } else if (primitive instanceof Relation) {
            Relation relation = (Relation) primitive;
            writer.write(String.format("    <relation id=\"%d\" version=\"%d\" timestamp=\"%s\">\n", relation.getUniqueId(), relation.getVersion(), relation.getInstant().toString()));
            for (RelationMember member : relation.getMembers()) {
                writer.write(String.format("      <member type=\"%s\" ref=\"%d\" role=\"%s\"/>\n", member.getType(), member.getUniqueId(), member.getRole()));
            }
            writeTags(writer, relation);
            writer.write("    </relation>\n");
        }
    }

    private static void writeTags(Writer writer, OsmPrimitive primitive) throws Exception {
        for (String key : primitive.keySet()) {
            writer.write(String.format("      <tag k=\"%s\" v=\"%s\"/>\n", key, primitive.get(key)));
        }
    }
}
