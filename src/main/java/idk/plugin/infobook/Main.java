package idk.plugin.infobook;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemWrittenBook;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main extends PluginBase implements Listener {

    private String bookName;
    private String bookAuthor;
    private List<String> text = new ArrayList<>();
    private List<String> playersReceivedBook;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        playersReceivedBook = getConfig().getStringList("playersReceivedBook");
        bookName = getConfig().getString("bookName", "").replace("§", "\u00A7");
        bookAuthor = getConfig().getString("bookAuthor", "").replace("§", "\u00A7");
        getConfig().getStringList("Lines").forEach((s) -> {
            text.add(s.replace("§", "\u00A7"));
        });
        if (text == null) text = Arrays.asList("");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String playerName = p.getName().toLowerCase();

        if (!playersReceivedBook.contains(playerName)) {
            for (Item i : p.getInventory().getContents().values()) {
                if (Objects.equals(i.getId(), "minecraft:written_book") && i.getDamage() == 0) return;
            }
            ItemWrittenBook book = (ItemWrittenBook) Item.get("minecraft:written_book", 0, 1);
            book.writeBook(bookAuthor, bookName, (text.size() <= 50 ? text.toArray(new String[text.size()]) : Arrays.copyOfRange(text.toArray(new String[text.size()]), 0, 50)));
            p.getInventory().addItem(book);

            playersReceivedBook.add(playerName);
            getConfig().set("playersReceivedBook", playersReceivedBook);
            saveConfig();
        }
    }
}
