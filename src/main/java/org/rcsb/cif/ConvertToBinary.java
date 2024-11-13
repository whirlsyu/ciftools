package org.rcsb.cif;

import org.rcsb.cif.model.CifFile;
import org.rcsb.cif.schema.StandardSchemata;
import org.rcsb.cif.schema.mm.MmCifFile;

import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.io.FileOutputStream;
import java.nio.file.Paths;

public class ConvertToBinary {

    private static String[] getFiles(String path) {
        String directoryPath = path;
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            String[] files = directory.list();
            return files;
        }
        return null;
    }

    private static void outFile(String path, String filename, byte[] data) {
        String filePath = path + "/" + filename;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            // write data to bin file
            fos.write(data);
            System.out.println("Data has been written to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String inputFilePath = "./input_cif/";
        String outputFilePath = "./output_bcif";

        String[] files = getFiles(inputFilePath);
        if (files == null) {
            System.out.println("No files found in " + inputFilePath);
            return;
        }

        for (String fileName : files) {
            System.out.println(fileName);

            Path cifFilePath = Paths.get(inputFilePath + fileName);
            CifFile cifFile = CifIO.readFromPath(cifFilePath);

            MmCifFile mmCifFile = cifFile.as(StandardSchemata.MMCIF);

            // convert to BinaryCIF representation
            byte[] output = CifIO.writeBinary(mmCifFile);

            outFile(outputFilePath, fileName.replace("cif", "bcif"), output);
        }
    }
}
