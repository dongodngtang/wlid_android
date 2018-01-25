package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.RecStatsEntity;

/**
 * Created by zaitu on 15-12-4.
 */
public class UserRecStats {

    /**
     * thisYearSpeciesNum : 4
     * recordNum : 5
     * thisYearRecordNum : 5
     * speciesNum : 4
     */

    private RecStatsEntity recStats;

    public void setRecStats(RecStatsEntity recStats) {
        this.recStats = recStats;
    }

    public RecStatsEntity getRecStats() {
        return recStats;
    }


}
