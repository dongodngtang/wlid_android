package net.doyouhike.app.wildbird.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import net.doyouhike.app.wildbird.biz.db.bean.DbWildBird;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.bean.Comment;
import net.doyouhike.app.wildbird.biz.model.bean.SpeciesInfo;
import net.doyouhike.app.wildbird.biz.service.database.WildbirdDbService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

@SuppressLint("HandlerLeak")
public class XMLParseUtil {

    public static final String path = Content.FILE_PATH_PARENT;
    public static final String FEATURE_PATH=path+"xml/";
    public static final String DATA_PATH=path+ "data/";
    public static final String FILE_NM_FEATURE ="birdFeatureList.xml";

    private List<Element> childList;// 一级子元素

    public XMLParseUtil(Context context) {
    }

    @SuppressWarnings("unchecked")
    public List<Element> getFeatures() throws DocumentException {
        // TODO Auto-generated method stub
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(FEATURE_PATH +FILE_NM_FEATURE));
        // 获取根元素
        Element root = document.getRootElement();
        // 获取所有子元素
        childList = root.elements();
        return childList;
    }

    @SuppressWarnings("unchecked")
    public void getFeature(Element element) {
        // TODO Auto-generated method stub
        SpeciesInfo info = new SpeciesInfo();
        info.setSpeciesID((String) element.element("speciesID").getData());
        try {
            info.setShape((String) element.element("shape").getData());
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }
        String str = "";
        try {
            List<Element> colorList = element.element("colorList").elements();
            for (int j = 0; j < colorList.size(); j++) {
                str += (String) colorList.get(j).getData() + "#";
            }
            info.setColorList(str);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }

        try {
            List<Element> locateList = element.element("locateList").elements();
            str = "";
            for (int j = 0; j < locateList.size(); j++) {
                str += (String) locateList.get(j).getData() + "#";
            }
            info.setLocateList(str);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }

        try {
            List<Element> behaviorList = element.element("behaviorList").elements();
            str = "";
            for (int j = 0; j < behaviorList.size(); j++) {
                str += (String) behaviorList.get(j).getData() + "#";
            }
            info.setBehaviorList(str);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }

        try {
            List<Element> headList = element.element("headList").elements();
            str = "";
            for (int j = 0; j < headList.size(); j++) {
                str += (String) headList.get(j).getData() + "#";
            }
            info.setHeadList(str);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }

        try {
            List<Element> neckList = element.element("neckList").elements();
            str = "";
            for (int j = 0; j < neckList.size(); j++) {
                str += (String) neckList.get(j).getData() + "#";
            }
            info.setNeckList(str);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }

        try {
            List<Element> bellyList = element.element("bellyList").elements();
            str = "";
            for (int j = 0; j < bellyList.size(); j++) {
                str += (String) bellyList.get(j).getData() + "#";
            }
            info.setBellyList(str);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }

        try {
            List<Element> waistList = element.element("waistList").elements();
            str = "";
            for (int j = 0; j < waistList.size(); j++) {
                str += (String) waistList.get(j).getData() + "#";
            }
            info.setWaistList(str);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }

        try {
            List<Element> wingList = element.element("wingList").elements();
            str = "";
            for (int j = 0; j < wingList.size(); j++) {
                str += (String) wingList.get(j).getData() + "#";
            }
            info.setWingList(str);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }

        try {
            List<Element> tailList = element.element("tailList").elements();
            str = "";
            for (int j = 0; j < tailList.size(); j++) {
                str += (String) tailList.get(j).getData() + "#";
            }
            info.setTailList(str);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }

        try {
            List<Element> legList = element.element("legList").elements();
            str = "";
            for (int j = 0; j < legList.size(); j++) {
                str += (String) legList.get(j).getData() + "#";
            }
            info.setLegList(str);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }
        try {
            List<Element> newlocateList = element.element("erLocateList").elements();
            str = "";
            for (int j = 0; j < newlocateList.size(); j++) {
                str += (String) newlocateList.get(j).getData() + "#";
            }
            info.setNewlocate(str);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
        }
//        db = helper.getWritableDatabase();
//        ControlDB.insertFeature(db, info);
        //使用greend更新数据库内容
        WildbirdDbService.getInstance().updateFeature(info);
    }

    @SuppressWarnings("unchecked")
    public void getBirds() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(FEATURE_PATH + "bird_base.xml"));
        // 获取根元素
        Element root = document.getRootElement();
        // 获取所有子元素
        childList = root.elements();
        for (int i = 0; i < childList.size(); i++) {
            Log.i("info", "parse " + i);
            DbWildBird info = new DbWildBird();
            info.setSpeciesID((String) childList.get(i).element("speciesID").getData());
            info.setLocalName((String) childList.get(i).element("localName").getData());
            info.setEngName((String) childList.get(i).element("engName").getData());
            info.setLatinName((String) childList.get(i).element("latinName").getData());

            try {

                info.setOrdo((String) childList.get(i).element("ordo").getData());
                info.setFamilia((String) childList.get(i).element("familia").getData());
                info.setGenus((String) childList.get(i).element("genus").getData());

                List<Element> commentList = childList.get(i).element("commentList").elements();
                for (int j = 0; j < commentList.size(); j++) {
                    Comment comment = new Comment();
                    comment.setCommentID(Long.parseLong((String) commentList.get(j).element("commentID").getData()));
                    comment.setUserID((String) commentList.get(j).element("userID").getData());
                    try {
                        comment.setUserName((String) commentList.get(j).element("userName").getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    comment.setLikeNum(Integer.parseInt((String) commentList.get(j).element("likeNum").getData()));
                    comment.setContent((String) commentList.get(j).element("content").getData());
                    comment.setTime((String) commentList.get(j).element("time").getData());
                    info.addComment(comment);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                List<Element> imageList = childList.get(i).element("imageList").elements();
                for (int j = 0; j < imageList.size(); j++) {
                    info.setImage((String) imageList.get(j).getData());
                    try {
                        info.setAuthor(imageList.get(j).attributeValue("author"));
                    } catch (Exception e) {
                        Log.i("info", e.getMessage());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //使用greendao生成数据库内容
            WildbirdDbService.getInstance().insertWildbirdInfo(info);
        }
        Log.i("info", "insert success");
        List<Element> childList = getFeatures();
        for (int i = 0; i < childList.size(); i++) {
            getFeature(childList.get(i));
        }
    }

    @SuppressWarnings("rawtypes")
    public static void upPhotoZip(Context context, File zipFile, String folderPath) {
        try {
            ZipFile zfile = new ZipFile(zipFile);
            Enumeration zList = zfile.entries();
            ZipEntry ze = null;
            byte[] buf = new byte[1024];
            while (zList.hasMoreElements()) {
                ze = (ZipEntry) zList.nextElement();
                if (ze.isDirectory()) {
                    String dirstr = folderPath + ze.getName();
                    dirstr.trim();
                    dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                    File f = new File(dirstr);
                    f.mkdir();
                    continue;
                }
                File file = new File(folderPath, ze.getName());
                if (!file.exists()) {
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                    int readLen = 0;
                    while ((readLen = is.read(buf, 0, 1024)) != -1) {
                        os.write(buf, 0, readLen);
                    }
                    is.close();
                    os.close();
                }
            }
            zfile.close();
            zipFile.delete();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }

    /**
     * 解压离线包
     *
     * @param context
     * @param zipFile
     * @param folderPath
     */
    @SuppressWarnings("rawtypes")
    public static void upZipFile(Activity context, File zipFile, String folderPath) {
        try {
            ZipFile zfile = new ZipFile(zipFile);
            Enumeration zList = zfile.entries();
            ZipEntry ze = null;
            byte[] buf = new byte[1024];
            File file = new File(folderPath, FILE_NM_FEATURE);
            if (file.exists()) file.delete();
            while (zList.hasMoreElements()) {
                ze = (ZipEntry) zList.nextElement();
                if (ze.isDirectory()) {
                    String dirstr = folderPath + ze.getName();
                    dirstr.trim();
                    dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                    File f = new File(dirstr);
                    f.mkdir();
                    continue;
                }
                OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(folderPath, FILE_NM_FEATURE)));
                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            }
            zfile.close();
            zipFile.delete();
            File f = new File(DATA_PATH);
            if (!f.exists()) f.mkdirs();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
