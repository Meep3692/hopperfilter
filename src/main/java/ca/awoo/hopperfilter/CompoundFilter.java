package ca.awoo.hopperfilter;

import java.util.Set;

import org.bukkit.Material;

public class CompoundFilter implements Filter {
    private final Set<Filter> positiveFilters;
    private final Set<Filter> negativeFilters;

    public CompoundFilter(Set<Filter> positiveFilters, Set<Filter> negativeFilters) {
        this.positiveFilters = positiveFilters;
        this.negativeFilters = negativeFilters;
    }

    @Override
    public boolean matches(Material material) {
        for (Filter filter : negativeFilters) {
            if (filter.matches(material)) {
                return false;
            }
        }
        for (Filter filter : positiveFilters) {
            if (filter.matches(material)) {
                return true;
            }
        }
        //If we only have negative filters
        if(positiveFilters.isEmpty())
            return true;
        return false;
    }


}
