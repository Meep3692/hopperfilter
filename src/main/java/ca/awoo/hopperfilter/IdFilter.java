package ca.awoo.hopperfilter;

import java.util.regex.Pattern;

import org.bukkit.Material;

public class IdFilter implements Filter{

    private final Pattern pattern;

    public IdFilter(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(Material material) {
        return pattern.matcher(material.getKey().getKey()).matches();
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
        IdFilter other = (IdFilter) obj;
        if (pattern == null) {
            if (other.pattern != null)
                return false;
        } else if (!pattern.equals(other.pattern))
            return false;
        return true;
    }
    
}
