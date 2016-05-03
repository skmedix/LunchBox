package org.bukkit.event.painting;

import org.bukkit.Warning;
import org.bukkit.entity.Painting;
import org.bukkit.event.Event;

/** @deprecated */
@Deprecated
@Warning(
    reason = "This event has been replaced by HangingEvent"
)
public abstract class PaintingEvent extends Event {

    protected Painting painting;

    protected PaintingEvent(Painting painting) {
        this.painting = painting;
    }

    public Painting getPainting() {
        return this.painting;
    }
}
