package be.rony.musicconverter;

/* libFLAC - Free Lossless Audio Codec library
 * Copyright (C) 2000,2001,2002,2003  Josh Coalson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jflac.FrameListener;
import org.jflac.FLACDecoder;
import org.jflac.frame.Frame;
import org.jflac.metadata.Metadata;
import org.jflac.metadata.Picture;
import org.jflac.metadata.VorbisComment;
import org.jflac.metadata.VorbisString;

/**
 * Analyser reads all metadata and frame blocks in a FLAC file and outputs a text
 * representation of them.
 * @author kc7bfi
 */
public class Analyser implements FrameListener {
    private int frameNum = 0;
    
    /**
     * Analyse an input FLAC file.
     * @param inFileName The input file name
     * @throws IOException thrown if error reading file
     */
    public void analyse(String inFileName) throws IOException {
        System.out.println("FLAX Analysis for " + inFileName);
        FileInputStream is = new FileInputStream(inFileName);
        FLACDecoder decoder = new FLACDecoder(is);
        decoder.addFrameListener(this);
        decoder.decode();
    }
    
    /**
     * Process metadata records.
     * @param metadata the metadata block
     * @see org.jflac.FrameListener#processMetadata(org.jflac.metadata.Metadata)
     */
    public void processMetadata(Metadata metadata) {
        if (metadata instanceof VorbisComment) {
            Map<String,String> tags = new HashMap<String,String>();
            VorbisComment vorbisComment = (VorbisComment) metadata;
            for (int i = 0; i < vorbisComment.getNumComments(); i++) {
                VorbisString vorbisString = vorbisComment.getComment(i);
                String[] arr = vorbisString.toString().split("=", 2);
                tags.put(arr[0].toUpperCase(),arr[1]);
            }
            /*
            TITLE=Thunder Road
            ARTIST=Bruce Springsteen
            ALBUMARTIST=Bruce Springsteen
            ALBUM=Greatest Hits
            DATE=1995
            TRACKNUMBER=02
            GENRE=Rock
            DESCRIPTION=Extra Comment
            COMPOSER=Composer
            PERFORMER=Bruce Orig
            COPYRIGHT=No copyricht
            */

            System.out.println(vorbisComment.toString());
        } else if (metadata instanceof Picture) {
            Picture picture = (Picture) metadata;
            System.out.println(picture.toString());
        }
        //System.out.println(metadata.toString());
    }
    
    /**
     * Process data frames.
     * @param frame the data frame
     * @see org.jflac.FrameListener#processFrame(org.jflac.frame.Frame)
     */
    public void processFrame(Frame frame) {
        frameNum++;
        System.out.println(frameNum + " " + frame.toString());
    }
   
    /**
     * Called for each frame error detected.
     * @param msg   The error message
     * @see org.jflac.FrameListener#processError(String)
     */
    public void processError(String msg) {
        System.out.println("Frame Error: " + msg);
    }
    
    /**
     * Main routine.
     * <p>args[0] is the FLAC file name to analyse
     * @param args  Command arguments
     */
    public static void main(String[] args) {
        try {
            Analyser analyser = new Analyser();
            analyser.analyse(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
