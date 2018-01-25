package net.doyouhike.app.wildbird.util;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.wildbird.biz.dao.net.NetException;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-15.
 */
public class StateUtil {

    public static UiState toUiState(NetException netException) {


        if (null != netException && netException.getState() != null) {


            switch (netException.getState()) {
                case NO_CONNECT:
                    return UiState.NETERR.setMsg(netException.getMessage()).setCode(netException.getCode());
                default:
                    return UiState.ERROR.setMsg(netException.getMessage()).setCode(netException.getCode());

            }

        }

        return UiState.ERROR;
    }
}
