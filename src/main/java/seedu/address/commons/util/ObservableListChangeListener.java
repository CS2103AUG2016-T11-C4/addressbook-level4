package seedu.address.commons.util;

import java.util.concurrent.atomic.AtomicBoolean;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * A utility class that tracks whether ObservableList(s) have changed.
 */
public class ObservableListChangeListener {

    // We use an AtomicBoolean because the event listeners may be called from another thread.
    private final AtomicBoolean hasChanged;

    public ObservableListChangeListener(ObservableList<?>... lists) {
        hasChanged = new AtomicBoolean(false);
        for (ObservableList<?> list : lists) {
            list.addListener((ListChangeListener.Change<? extends Object> change) -> {
                hasChanged.set(true);
            });
        }
    }

    public void setHasChanged(boolean hasChanged) {
        this.hasChanged.set(hasChanged);
    }

    public boolean getHasChanged() {
        return hasChanged.get();
    }

}
