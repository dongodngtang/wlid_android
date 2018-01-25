package net.doyouhike.app.wildbird.biz.model.event;

/**
 * Created by zaitu on 15-11-19.
 */
public interface IObserver {
    void onWbEvent(BaseEvent event);
}
