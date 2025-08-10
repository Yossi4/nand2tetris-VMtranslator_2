import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CodeWriter {

    private PrintWriter outFile; 
    private int jumpLoop = 0; // "Indector" of a loop somewhere
    private String fileName = ""; 
    private String functionLabel; 
    private int labelCounter = 0; 

    public CodeWriter(File file) {
        try {
            outFile = new PrintWriter(file);
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    







    public void setFileName(String file_name) {
        fileName = file_name.substring(0, file_name.indexOf('.'));
    }

    // Writes o the output file the assembly code:
    public void writeArithmetic(String cmd) {
        String template = "@SP\n" +"AM=M-1\n" +"D=M\n" +"A=A-1\n";
        if(cmd.equals("add")) {
            outFile.print(template + "M=D+M\n");
        }

        else if(cmd.equals("sub")) {
            outFile.print(template + "M=M-D\n");
        }

        else if(cmd.equals("and")) {
            outFile.print(template + "M=D&M\n");
        }

        else if(cmd.equals("or")) {
            outFile.print(template + "M=D|M\n");
        }

        else if(cmd.equals("gt")) {
            outFile.print(arithmeticLogicalTemplate("JLE"));
            jumpLoop++;
        }

        else if(cmd.equals("lt")) {
            outFile.print(arithmeticLogicalTemplate("JGE"));
            jumpLoop++;
        }

        else if(cmd.equals("eq")) {
            outFile.print(arithmeticLogicalTemplate("JNE"));
            jumpLoop++;
        }

        else if(cmd.equals("not")) {
            outFile.print("@SP\nA=M-1\nM=!M\n");
        }
        
        else if(cmd.equals("neg")) {
            outFile.print("D=0\n@SP\nA=M-1\nM=D-M\n");
        }

        else {
            throw new IllegalArgumentException("Call writeArithmetic() for a non-arithmetic command");
        }
    }

    public void writePushPop(int command, String seg, int index) {
        if(command == Parser.C_PUSH) {

            if(seg.equals("constant")) {
                outFile.print("@" + index + "\n" + "D=A\n");
            }

            else if(seg.equals("local")) {
                outFile.print("@LCL\n" + "D=M\n");
                outFile.print("@" + index + "\n" + "D=D+A\n" + "@addr" + index + "\n" + "M=D\n" + "@addr" + index + "\n" + "A=M\n" + "D=M\n");
            }

            else if(seg.equals("argument")) {
                outFile.print("@ARG\n" + "D=M\n");
                outFile.print("@" + index + "\n" + "D=D+A\n" + "@addr" + index + "\n" + "M=D\n" + "@addr" + index + "\n" + "A=M\n" + "D=M\n");
            }

            else if(seg.equals("this")) {
                outFile.print("@THIS\n" + "D=M\n");
                outFile.print("@" + index + "\n" + "D=D+A\n" + "@addr" + index + "\n" + "M=D\n" + "@addr" + index + "\n" + "A=M\n" + "D=M\n");
            }

            else if(seg.equals("that")){
                outFile.print("@THAT\n" + "D=M\n");
                outFile.print("@" + index + "\n" + "D=D+A\n" + "@addr" + index + "\n" + "M=D\n" + "@addr" + index + "\n" + "A=M\n" + "D=M\n");
            }

            else if(seg.equals("temp")) {
                outFile.print("@5\n" + "D=A\n");
                outFile.print("@" + index + "\n" + "D=D+A\n" + "@addr" + index + "\n" + "M=D\n" + "@addr" + index + "\n" + "A=M\n" + "D=M\n");
            }

            else if(seg.equals("pointer") && index == 0) {
                outFile.print("@THIS\n" + "D=M\n");
            }

            else if(seg.equals("pointer") && index == 1) {
                outFile.print("@THAT\n" + "D=M\n");
            }

            else if (seg.equals("static")) {
                outFile.print("@" + fileName + "." + index + "\n" + "D=M\n");
            }

            outFile.print("@SP\n" + "A=M\n" + "M=D\n");
            outFile.print("@SP\n"+ "M=M+1\n");
        }

        else if(command == Parser.C_POP) {

            if(seg.equals("local")) {
                outFile.print("@LCL\n" + "D=M\n");
                outFile.print("@" + index + "\n" + "D=D+A\n" + "@addr" + index + "\n" + "M=D\n");
                outFile.print("@SP\n" + "AM=M-1\n" + "D=M\n");
                outFile.print("@addr" + index + "\n" + "A=M\n");
            }

            else if(seg.equals("argument")) {
                outFile.print("@ARG\n" + "D=M\n");
                outFile.print("@" + index + "\n" + "D=D+A\n" + "@addr" + index + "\n" + "M=D\n");
                outFile.print("@SP\n" + "AM=M-1\n" + "D=M\n");
                outFile.print("@addr" + index + "\n" + "A=M\n");
            }

            else if(seg.equals("this")) {
                outFile.print("@THIS\n" + "D=M\n");
                outFile.print("@" + index + "\n" + "D=D+A\n" + "@addr" + index + "\n" + "M=D\n");
                outFile.print("@SP\n" + "AM=M-1\n" + "D=M\n");
                outFile.print("@addr" + index + "\n" + "A=M\n");
            }

            else if(seg.equals("that")) {
                outFile.print("@THAT\n" + "D=M\n");
                outFile.print("@" + index + "\n" + "D=D+A\n" + "@addr" + index + "\n" + "M=D\n");
                outFile.print("@SP\n" + "AM=M-1\n" + "D=M\n");
                outFile.print("@addr" + index + "\n" + "A=M\n");  
            }

            else if(seg.equals("temp")) {
                outFile.print("@temp\n" + "@5\n" + "D=A\n");
                outFile.print("@" + index + "\n" + "D=D+A\n" + "@addr" + index + "\n" + "M=D\n");
                outFile.print("@SP\n" + "AM=M-1\n" + "D=M\n");
                outFile.print("@addr" + index + "\n" + "A=M\n");
            }

            else if(seg.equals("pointer") && index == 0) {
                outFile.print("@SP\n" + "AM=M-1\n" + "D=M\n" + "@THIS\n");

            }

            else if(seg.equals("pointer") && index == 1) {
                outFile.print("@SP\n" + "AM=M-1\n" + "D=M\n" + "@THAT\n");
            }

            else if(seg.equals("static")) {
                outFile.print("@SP\n" + "AM=M-1\n" + "D=M\n");
                outFile.print("@" + fileName + "." + index + "\n");
            }
            outFile.print("M=D\n");
        }
        else{
            throw new IllegalArgumentException("Call writePushPop() for a non-pushpop command");
        }
    }

    public void writeLabel(String label) {
        outFile.print("(" + label + ")\n");
        
    }

    public void writeGoto(String label) {
        outFile.print("@" + label + "\n" + "0;JMP\n");
    }

    public void writeIf(String label) {
        outFile.print("@SP\n" + "AM=M-1\n" + "D=M\n");
        outFile.print("@" + label + "\n" + "D;JNE\n");
    }

    public void writeFunction(String functionName, int nVars) {
        this.functionLabel= functionName;
        writeLabel(functionName);
        for(int i=0; i< nVars; i++) {
            outFile.print("@LCL\n" + "D=M\n" + "@"+ i + "\n" + "A=D+A\n" + "M=0\n");
            outFile.print("@SP\n" + "M=M+1\n");
        }
    }

    public void writeCall(String functionName, int nArgs) {
        outFile.print("@" + functionLabel + "$ret." + labelCounter +"\n" + "D=A\n");
        outFile.print("@SP\n" + "A=M\n" + "M=D\n");
        outFile.print("@SP\n" + "M=M+1\n");
        for(String seg : new String[] {"LCL","ARG","THIS","THAT"}) {
            outFile.write("@" + seg +"\n" + "D=M\n");
            outFile.print("@SP\n" + "A=M\n" + "M=D\n");
            outFile.print("@SP\n" + "M=M+1\n");
        }
        outFile.print("@SP\n" + "D=M\n" + "@5\n" + "D=D-A\n" + "@" + nArgs + "\n" + "D=D-A\n" + "@ARG\n" + "M=D\n");
        outFile.print("@SP\n" + "D=M\n" + "@LCL\n" + "M=D\n");
        writeGoto(functionName);
        writeLabel(functionLabel + "$ret." + labelCounter);
        labelCounter++;
    }

    public void writeReturn() {
        outFile.print("@LCL\n" + "D=M\n" + "@frame\n" + "M=D\n");
        outFile.print("@frame\n" + "D=M\n" + "@5\n" + "A=D-A\n" + "D=M\n" + "@retAddr\n" + "M=D\n");
        outFile.print("@SP\n" + "AM=M-1\n" + "D=M\n");
        outFile.print("@ARG\n" + "A=M\n" + "M=D\n");
        outFile.print("@ARG\n" + "D=M+1\n" + "@SP\n" + "M=D\n");
        for(String seg : new String[] {"THAT","THIS","ARG","LCL"}) {
            outFile.print("@frame\n" + "AM=M-1\n" + "D=M\n" + "@" + seg + "\n" + "M=D\n");
        }
        outFile.print("@retAddr\n" + "A=M\n" + "0;JMP\n");
    }

    // Closes the output file:
    public void close() {
        outFile.close();
    }

    private String arithmeticLogicalTemplate(String type) {
        return "@SP\n" + "AM=M-1\n" + "D=M\n" + "A=A-1\n" + "D=M-D\n" + "@FALSE" + jumpLoop + "\n" + "D;" + type + "\n" + "@SP\n" +
                "A=M-1\n" + "M=-1\n" + "@CONTINUE" + jumpLoop + "\n" + "0;JMP\n" + "(FALSE" + jumpLoop + ")\n" + "@SP\n" + "A=M-1\n" +
                "M=0\n" + "(CONTINUE" + jumpLoop + ")\n";
    }

    //Boostrap:
    public void writeInit(){
        outFile.print("@256\n" + "D=A\n" + "@SP\n" + "M=D\n");
        writeCall("Sys.init",0);

    }
}


