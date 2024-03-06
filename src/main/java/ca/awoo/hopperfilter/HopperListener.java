package ca.awoo.hopperfilter;

import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class HopperListener implements Listener {

    HashMap<String, Filter> filterCache;

    public HopperListener(){
        filterCache = new HashMap<>();
    }

    boolean filterMatch(String filterString, Material item){
        Filter filter = filterCache.get(filterString);
        if(filter == null){
            filter = filterStringToFilter(filterString);
            filterCache.put(filterString, filter);
        }
        return filter.matches(item);
    }

    Filter filterStringToFilter(String filterString){
        String[] sections = filterString.split(",");
        Set<Filter> positiveFilters = new HashSet<>();
        Set<Filter> negativeFilters = new HashSet<>();
        for(String section : sections){
            if(section.startsWith("!")){
                negativeFilters.add(parseFilter(section.substring(1)));
            }else{
                positiveFilters.add(parseFilter(section));
            }
        }
        return new CompoundFilter(positiveFilters, negativeFilters);
    }

    Filter parseFilter(String filterString){
        if(filterString.startsWith("#")){
            return new TagFilter(Pattern.compile(wildcardToRegex(filterString.substring(1))));
        }else{
            return new IdFilter(Pattern.compile(wildcardToRegex(filterString)));
        }
    }

    String wildcardToRegex(String query){
        String[] sections = query.split("[*?]");
        StringBuilder sb = new StringBuilder();
        sb.append("^");
        int pos = 0;
        for(String section : sections){
            sb.append(Pattern.quote(section));
            pos += section.length();
            try {
                char wildcard = query.charAt(pos);
                if(wildcard == '*'){
                    sb.append(".*");
                }else if(wildcard == '?'){
                    sb.append(".");
                }
            }catch(IndexOutOfBoundsException _){}
        }
        sb.append("$");
        return sb.toString();
    }

    @EventHandler
    void onInventoryMoveItemEvent(InventoryMoveItemEvent event){
        if(event.getDestination().getType().equals(InventoryType.HOPPER) && event.getDestination().getHolder() instanceof Container){
            String customName = ((Container) event.getDestination().getHolder()).getCustomName();
            if(customName != null){
                Material material = event.getItem().getType();
                if(!filterMatch(customName, material)){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    void onInventoryPickupItemEvent(InventoryPickupItemEvent event) {
        if(event.getInventory().getHolder() instanceof Container){
            String customName = ((Container)event.getInventory().getHolder()).getCustomName();
            if(customName != null){
                Material material = event.getItem().getItemStack().getType();
                if(!filterMatch(customName, material)){
                    event.setCancelled(true);
                }
            }
        }
    }

    /*@EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getItemInHand().getType().equals(Material.HOPPER) && event.getItemInHand().getItemMeta().hasDisplayName()){
            String customName = event.getItemInHand().getItemMeta().getDisplayName();
            try{
                Pattern.compile(customName);
            }catch(PatternSyntaxException e){
                event.getPlayer().sendMessage("Invalid regex: " + customName);
            }
        }
    }*/
}
