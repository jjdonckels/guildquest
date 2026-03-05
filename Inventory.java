package guildquest.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * REFACTORING: Encapsulate Collection (Fowler / lecture slide 71).
 *
 * MOTIVATION: The original Inventory exposed a raw mutable List via getItems().
 * Any caller could call getItems().clear() or getItems().add(...) bypassing any
 * future business rules (e.g., weight limits, duplicate checks). This is an
 * "Inappropriate Intimacy" smell — callers know too much about internal storage.
 * The fix is to return an unmodifiable view and route all mutations through
 * explicit addItem / removeItem methods, which can enforce invariants in one
 * place without changing existing callers that only read the list.
 */
public class Inventory {

    private final List<InventoryItem> items = new ArrayList<>();

    public void addItem(InventoryItem item) {
        items.add(item);
    }

    public void removeItem(InventoryItem item) {
        items.remove(item);
    }

    public void updateItem(InventoryItem item, String newName, String newType, int newValue) {
        item.setName(newName);
        item.setType(newType);
        item.setValue(newValue);
    }

    /** Returns an unmodifiable view — callers must use add/remove to mutate. */
    public List<InventoryItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int size() { return items.size(); }
}
