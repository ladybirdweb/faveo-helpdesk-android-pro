package co.helpdesk.faveo.pro;

import android.content.Context;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * File Cacher is for caching an object.
 *
 * Created by Oum Saokosal (2016)
 * Source Code at github.com/kosalgeek/FileCacher
 * YouTube Channel at youtube.com/user/oumsaokosal
 * Facebook Page at facebook.com/kosalgeek
 *
 * @param <T> generic T type
 */
public class FileCacher<T>{
    private final String LOG = this.getClass().getName();

    private Context context;
    private String fileName;

    /**
     * Create an instance of FileCacher
     * @param context App context
     * @param fileName fileName
     */
    public FileCacher(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    /**
     * Overwrite an object to cache. It simply deletes a old file and create a new one. Use it carefully.
     *
     * @param object any object which could be anything, e.g. int, String[], ArrayList<Object> etc.
     * @throws IOException
     */
    public void writeCache(T object) throws IOException {
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.flush();
        oos.close();

        fos.close();
    }

    /**
     * Append an object if cache file exists. Otherwise, create a new file and write an object.
     * @param object any object which could be anything, e.g. int, String[], ArrayList<Object> etc.
     * @throws IOException
     */
    public void appendOrWriteCache(T object) throws IOException {
        FileOutputStream fos;
        ObjectOutputStream oos;
        if(hasCache()){
            fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            oos = new ObjectOutputStream(fos){
                protected void writeStreamHeader() throws IOException {
                    reset();
                }
            };
        }
        else{
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
        }
        oos.writeObject(object);
        oos.flush();
        oos.close();

        fos.close();
    }

    /**
     * Read object back from the cache.
     * Make sure it is not NULL before using it.
     *
     * @return cache object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public T readCache() throws IOException {
        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = null;
        try {
            object = ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        fis.close();
        return (T)object;
    }

    /**
     * get all caches
     * @return List of caches
     * @throws IOException
     */
    public List<T> getAllCaches() throws IOException {
        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = null;
        List<T> cacheList = new ArrayList<T>();
        try {
            while((object = ois.readObject()) != null){
                cacheList.add((T) object);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException e){
            fis.close();
        }

        return cacheList;
    }

    /**
     * delete cache file
     * @throws IOException
     */
    public boolean clearCache() throws IOException {
        boolean success = context.deleteFile(fileName);
        if(success){
            return true;
        }
        return false;
    }

    /**
     * get file path
     * @return String of file path
     */
    public String getFilePath(){
        return context.getFileStreamPath(fileName).getPath();
    }

    /**
     * Check whether the cache exists or not.
     *
     * @return 'true' if cache exists or 'false' if cache does not exists.
     */
    public boolean hasCache(){
        File file = context.getFileStreamPath(fileName);
        if(file.exists()){
            return true;
        }
        return false;
    }

    /**
     * get cache size
     * @return long of cache size
     */
    public long getSize() {
        long size = -1;
        File file = context.getFileStreamPath(fileName);

        if(file.exists()){
            size=file.length();
        }

        return size;
    }
}
