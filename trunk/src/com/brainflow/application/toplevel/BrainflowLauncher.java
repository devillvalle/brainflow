package com.brainflow.application.toplevel;

import uk.co.hcsoftware.yago.Option;
import uk.co.hcsoftware.yago.parsers.GnuParser;
import uk.co.hcsoftware.yago.util.DefaultArgReaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 19, 2007
 * Time: 9:03:45 PM
 */
public class BrainflowLauncher {


    static Option<File> input = new Option(
            "f"                 //name (short-id defaults to this)
            , false               //is required
            , DefaultArgReaders.createFileReader(true) //treat the arg as a string
            , "list of files to view");    //description for the usage


    public static void main(String[] args) {
        GnuParser gp = null;


        try {
            gp = new GnuParser(input);
            gp.parse(args);

            input.getArgValue();
            List<String> input = gp.getExtraArgs();
            List<File> fileInput = new ArrayList<File>();

            for (String str : input) {
                File f = new File(str);
                if (!f.exists()) {
                    throw new FileNotFoundException(str + " not found");
                }

                fileInput.add(f);
            }

            Brainflow instance = Brainflow.getInstance();
            // instance.launch(fileInput);


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(gp.getUsage("Brainflow", e.getMessage()));
        }
    }
}
