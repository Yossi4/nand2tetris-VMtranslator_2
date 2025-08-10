import java.io.File;
import java.util.ArrayList;

public class Main {

    // The function goes through **all** the files in the folder and takes only the .vm files
    public static ArrayList<File> getVMFiles(File dirctorey) {
        File [] files = dirctorey.listFiles();
        ArrayList<File> result = new ArrayList<File>();
        for (File f:files){

            if (f.getName().endsWith(".vm")){
                result.add(f);
            }
        }
            return result;

    }
    
    public static void main(String[] args) {
        File fileOut;
        String fileOutPath = "";
        String inputFile = args[0];
        File filein = new File(inputFile);
        CodeWriter write = null;
        ArrayList<File> vmFiles = new ArrayList<File>();
        if(args.length != 1){
            System.out.println("no file insert");
        }

        else {
            //Checks for a SINGLE file
            if(filein.isFile()) {
                String path = filein.getAbsolutePath();

                if (!path.substring(path.lastIndexOf('.')).equals(".vm")) {

                    throw new IllegalArgumentException(".vm file is required!");
                }
                vmFiles.add(filein);
                fileOutPath = filein.getAbsolutePath().substring(0, filein.getAbsolutePath().lastIndexOf(".")) + ".asm";
                fileOut = new File(fileOutPath);
                write = new CodeWriter(fileOut);
            }







            //Checks if directorry
            else if(filein.isDirectory()) {
                vmFiles = getVMFiles(filein);
                if (vmFiles.size() == 0) {
                    throw new IllegalArgumentException("No vm file in this directory");
                }
                fileOutPath = filein.getAbsolutePath() + "/" +  filein.getName() + ".asm";
                fileOut = new File(fileOutPath);
                write = new CodeWriter(fileOut);
                write.writeInit();
            }
        }
        
        
        // Now we'll translate all files:
        for(File f : vmFiles) {
            write.setFileName(f.getName());
            Parser parser = new Parser(f);
            int type = -1;
            while(parser.hasMoreLines()) {
                parser.advance();
                type = parser.commandType();
                if(type == Parser.C_ARITHMETIC) {
                    write.writeArithmetic(parser.arg1());
                }
                else if(type == Parser.C_POP || type == Parser.C_PUSH) {
                        write.writePushPop(type, parser.arg1(), parser.arg2());
                }
                else if (type == Parser.C_LABEL) {
                        write.writeLabel(parser.arg1());
                } 
                else if (type == Parser.C_GOTO) {
                        write.writeGoto(parser.arg1());
                } 
                else if (type == Parser.C_IF) {
                    write.writeIf(parser.arg1());

                } 
                else if (type == Parser.C_RETURN) {
                    write.writeReturn();
                } 
                else if (type == Parser.C_FUNCTION) {
                    write.writeFunction(parser.arg1(),parser.arg2());
                } 
                else if (type == Parser.C_CALL) {
                    write.writeCall(parser.arg1(),parser.arg2());
                }
            }
        }
        write.close();
    }
            
}

