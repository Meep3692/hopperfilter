package ca.awoo.hopperfilter;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;

public class TagFilter implements Filter {
    private final Pattern pattern;

    public TagFilter(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(Material material) {
        //Check item tags
        Iterable<Tag<Material>> material_tags = Bukkit.getTags(Tag.REGISTRY_ITEMS, Material.class);
        for (Tag<Material> tag : material_tags) {
            if (pattern.matcher(tag.getKey().getKey()).matches()) {
                if (tag.isTagged(material)) {
                    return true;
                }
            }
        }
        //Check block tags if applicable
        if (material.isBlock()) {
            Iterable<Tag<Material>> block_tags = Bukkit.getTags(Tag.REGISTRY_BLOCKS, Material.class);
            for (Tag<Material> tag : block_tags) {
                if (pattern.matcher(tag.getKey().getKey()).matches()) {
                    if (tag.isTagged(material)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TagFilter other = (TagFilter) obj;
        if (pattern == null) {
            if (other.pattern != null)
                return false;
        } else if (!pattern.equals(other.pattern))
            return false;
        return true;
    }

    
}
