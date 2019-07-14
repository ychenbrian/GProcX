package gprocx;

import ro.sync.exml.plugin.Plugin;
import ro.sync.exml.plugin.PluginDescriptor;

/**
 * Workspace access plugin. 
 */
public class GProcXPlugin extends Plugin {
  /**
   * The static plugin instance.
   */
  private static GProcXPlugin instance = null;

  /**
   * Constructs the plugin.
   * 
   * @param descriptor The plugin descriptor
   */
  public GProcXPlugin(PluginDescriptor descriptor) {
    super(descriptor);

    if (instance != null) {
      throw new IllegalStateException("Already instantiated!");
    }
    instance = this;
  }
  
  /**
   * Get the plugin instance.
   * 
   * @return the shared plugin instance.
   */
  public static GProcXPlugin getInstance() {
    return instance;
  }
}