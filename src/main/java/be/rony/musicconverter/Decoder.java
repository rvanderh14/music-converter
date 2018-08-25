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

import java.awt.*;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import be.rony.musicconverter.domain.TagInfo;
import org.jflac.FLACDecoder;
import org.jflac.FrameListener;
import org.jflac.PCMProcessor;
import org.jflac.frame.Frame;
import org.jflac.metadata.*;
import org.jflac.util.WavWriter;
import org.jflac.util.ByteData;


/**
 * Decode FLAC file to WAV file application.
 * @author kc7bfi
 */
public class Decoder implements PCMProcessor, FrameListener {

    private int frameNum = 0;
    private WavWriter wav;

    private Picture picture;
    private Map<String,String> tags;

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

    /**
     * Decode a FLAC file to a WAV file.
     * @param inFileName    The input FLAC file name
     * @param outFileName   The output WAV file name
     * @throws IOException  Thrown if error reading or writing files
     */
    public void decode(String inFileName, String outFileName) throws IOException {
        System.out.println("Decode [" + inFileName + "][" + outFileName + "]");
        FileInputStream is = new FileInputStream(inFileName);
        FileOutputStream os = new FileOutputStream(outFileName);
        wav = new WavWriter(os);
        FLACDecoder decoder = new FLACDecoder(is);
        decoder.addPCMProcessor(this);
        decoder.addFrameListener(this);
        decoder.decode();
    }
    
    /**
     * Process the StreamInfo block.
     * @param info the StreamInfo block
     * @see org.jflac.PCMProcessor#processStreamInfo(org.jflac.metadata.StreamInfo)
     */
    public void processStreamInfo(StreamInfo info) {
        try {
            System.out.println("Write WAV header " + info);
            wav.writeHeader(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Process the decoded PCM bytes.
     * @param pcm The decoded PCM data
     * @see org.jflac.PCMProcessor#processPCM(org.jflac.util.ByteData)
     */
    public void processPCM(ByteData pcm) {
        try {
            System.out.println("Write PCM");
            wav.writePCM(pcm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process metadata records.
     * @param metadata the metadata block
     * @see org.jflac.FrameListener#processMetadata(org.jflac.metadata.Metadata)
     */
    public void processMetadata(Metadata metadata) {
        if (metadata instanceof VorbisComment) {
            tags = new HashMap<String,String>();
            VorbisComment vorbisComment = (VorbisComment) metadata;
            for (int i = 0; i < vorbisComment.getNumComments(); i++) {
                VorbisString vorbisString = vorbisComment.getComment(i);
                String[] arr = vorbisString.toString().split("=", 2);
                tags.put(arr[0].toUpperCase(),arr[1]);
            }
            // System.out.println(vorbisComment.toString());
        } else if (metadata instanceof Picture) {
            picture = (Picture) metadata;
            // System.out.println(picture.toString());
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
        //System.out.println(frameNum + " " + frame.toString());
    }

    /**
     * Called for each frame error detected.
     * @param msg   The error message
     * @see org.jflac.FrameListener#processError(String)
     */
    public void processError(String msg) {
        System.out.println("Frame Error: " + msg);
    }


    public TagInfo getTagInfo() {
       return null; // todo
    }

}
