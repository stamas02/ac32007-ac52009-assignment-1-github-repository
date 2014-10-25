/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;

import com.datastax.driver.core.utils.Bytes;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 *
 * @author Administrator
 */
public class Pic
{

    private ByteBuffer bImage = null;
    private int length = 0;
    private String type ="";
    private java.util.UUID UUID=null;
    private String folder ="";
    private int accessability = 0;
    private String picauthor = "";
    
    
    public void Pic() {

    }
    
    public void setFolder(String folder )
    {
    	this.folder = folder;
    }
    
    public String getFolder()
    {
    	return this.folder;
    }
    
    public void setPicAuthor(String picauthor )
    {
    	this.picauthor = picauthor;
    }
    
    public String getPicAuthor()
    {
    	return this.picauthor;
    }
    
    public void setAccessability(int a )
    {
    	this.accessability = a;
    }
    
    public int getAccessability()
    {
    	return this.accessability;
    }
    
    
    public void setUUID(java.util.UUID UUID){
        this.UUID =UUID;
    }
    public String getSUUID(){
        return UUID.toString();
    }
    public void setPic(ByteBuffer bImage, int length,String type, String folder, String picauthor) 
    {
        this.bImage = bImage;
        this.length = length;
        this.type=type;
        this.folder= folder;
        this.picauthor = picauthor;
    }

    

    public int getLength() {
        return length;
    }
    
    public String getType(){
        return type;
    }

    public ByteBuffer getBuffer() {
        return bImage;
    }
    
    public byte[] getBytes() 
    {
        if (bImage == null)
        	return null;
        byte image[] = Bytes.getArray(bImage);
        return image;
    }



}
