package ca.awoo.hopperfilter;

import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class HopperListener implements Listener {

    HashMap<String, Pattern> patternCache;

    public HopperListener(){
        patternCache = new HashMap<>();
    }

    boolean filterMatch(String filterString, String fullItemName){
        Pattern p;
        if(patternCache.containsKey(filterString)){
            p = patternCache.get(filterString);
        }else {
            try {
                String regexString = wildcardToRegex(filterString);
                p = Pattern.compile(regexString);
                patternCache.put(filterString, p);
            }catch(PatternSyntaxException e){
                return false;
            }
        }
        Matcher m = p.matcher(fullItemName);
        return m.find();
    }

    String wildcardToRegex(String query){
        String[] sections = query.split("[\\*\\?]");
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
            }catch(IndexOutOfBoundsException _){};
        }
        sb.append("$");
        return sb.toString();
    }

    @EventHandler
    void onInventoryMoveItemEvent(InventoryMoveItemEvent event){
        if(event.getDestination().getType().equals(InventoryType.HOPPER) && event.getDestination().getHolder() instanceof Container){
            String customName = ((Container) event.getDestination().getHolder()).getCustomName();
            if(customName != null){
                String itemName = event.getItem().getType().getKey().getKey();
                if(!filterMatch(customName, itemName)){
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
                String itemName = event.getItem().getItemStack().getType().getKey().getKey();
                if(!filterMatch(customName, itemName)){
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
