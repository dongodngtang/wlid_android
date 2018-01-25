package net.doyouhike.app.wildbird.biz.db.bean;

import net.doyouhike.app.wildbird.biz.db.dao.ImageDao;
import net.doyouhike.app.wildbird.biz.db.dao.WbDaoSession;
import net.doyouhike.app.wildbird.biz.db.dao.WildBirdDao;
import net.doyouhike.app.wildbird.biz.model.bean.Image;

import de.greenrobot.dao.DaoException;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "IMAGE".
 */
public class DbImage extends Image{

    private String speciesID;

    /** Used to resolve relations */
    private transient WbDaoSession daoSession;
    private Long id;
    /** Used for active entity operations. */
    private transient ImageDao myDao;
    private DbWildBird wildBird;
    private String wildBird__resolvedKey;


    public DbImage() {
    }

    public DbImage(Long id,String url, String author, String speciesID) {
        super(url,author);
        this.speciesID = speciesID;
        this.id=id;
    }

    public DbImage(Image image, String speciesID) {
        super(image.getUrl(),image.getAuthor());
        this.speciesID = speciesID;
        this.id=null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(WbDaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getImageDao() : null;
    }

    public String getSpeciesID() {
        return speciesID;
    }

    public void setSpeciesID(String speciesID) {
        this.speciesID = speciesID;
    }

    /** To-one relationship, resolved on first access. */
    public DbWildBird getWildBird() {
        String __key = this.speciesID;
        if (wildBird__resolvedKey == null || wildBird__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WildBirdDao targetDao = daoSession.getWildBirdDao();
            DbWildBird wildBirdNew = targetDao.load(__key);
            synchronized (this) {
                wildBird = wildBirdNew;
            	wildBird__resolvedKey = __key;
            }
        }
        return wildBird;
    }

    public void setWildBird(DbWildBird wildBird) {
        synchronized (this) {
            this.wildBird = wildBird;
            speciesID = wildBird == null ? null : wildBird.getSpeciesID();
            wildBird__resolvedKey = speciesID;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}