package org.openstreetmap.josm.plugins.patchExport;

import org.openstreetmap.josm.actions.ExtensionFileFilter;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

/**
 *
 * PatchExport Plugin
 * @author CZBackendUser1234
 *
 */
public class PatchExportPlugin extends Plugin {

    public PatchExportPlugin(PluginInformation info) {
        super(info);
        ExtensionFileFilter.addExporterFirst(new ChangesetExporter());
    }
}
