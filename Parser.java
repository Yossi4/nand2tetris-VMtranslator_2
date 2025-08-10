import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;


/* Here is Parser Class. 
 * This class Handels the parsing of a single .vm file.
 * This class reads a VM command, parses the command into its lexical componets and provides convenint access to these components.
 * The class ignores white space and comments.
 */
public class Parser {
    private static final HashMap<Integer,String> arithmetic_logical_commands = new HashMap<Integer,String>(); // The arithmetic-logical commands are arranged in a hashmap.
    private Scanner scan; 
    private String current; 


    // Instead of the formerly implemented enum:

    public static final int C_ARITHMETIC = 0; 
    public static final int C_PUSH = 1; 
    public static final int C_POP = 2; 
    public static final int C_LABEL = 3; 
    public static final int C_GOTO = 4; 
    public static final int C_IF = 5; 
    public static final int C_FUNCTION = 6; 
    public static final int C_RETURN = 7; 
    public static final int C_CALL = 8; 
    private String args1; 
    private int args2; 
    private int indextype;

    //Adding the types of the arithmetic operations to hashmap.
    static {
        arithmetic_logical_commands.put(1, "add");
        arithmetic_logical_commands.put(2, "sub");
        arithmetic_logical_commands.put(3, "neg");
        arithmetic_logical_commands.put(4, "eq");
        arithmetic_logical_commands.put(5, "gt");
        arithmetic_logical_commands.put(6, "lt");
        arithmetic_logical_commands.put(7, "and");
        arithmetic_logical_commands.put(8, "or");
        arithmetic_logical_commands.put(9, "not");
    }

    public Parser(File file) {
        try {
        this.scan = new Scanner(file);
        String preline = "";
        String line = "";
        while(scan.hasNext()) {
            line = noComments(scan.nextLine()).trim();
            if(!line.equals("")) {
                preline += line +"\n";
            }
        }
        this.scan = new Scanner(preline.trim());
        }

        catch(FileNotFoundException e) {
            System.out.println("file not found");
        }
    }

    public boolean hasMoreLines() {
        return scan.hasNextLine();
    }

    
    public void advance() {
        this.current = scan.nextLine();
        this.args1 = "";
        this.args2 = -1;
        String[] segment = current.split(" ");
        if(segment.length > 3){
            throw new IllegalArgumentException("Too much arguments");
        }

        if(arithmetic_logical_commands.containsValue(segment[0])) {
            indextype = C_ARITHMETIC;
            args1 = segment[0];
        }

        else if(segment[0].equals("return")){
            indextype = C_RETURN;
            args1 = segment[0];
        }

        else {
            args1 = segment[1];
            if(segment[0].equals("push")) {
                indextype = C_PUSH;
            }

            else if(segment[0].equals("pop")) {
                indextype = C_POP;
            }

            else if(segment[0].equals("label")) {
                indextype = C_LABEL;
            }
            
            else if(segment[0].equals("if-goto")) {
                indextype = C_IF;
            }
            else if(segment[0].equals("goto")) {
                indextype = C_GOTO;
            }
            
            else if(segment[0].equals("function")) {
                indextype = C_FUNCTION;
            }
            
            else if(segment[0].equals("call")) {
                indextype = C_CALL;
            }
            else {
                throw new IllegalArgumentException("Unknown Command Type");
            }
        }
        
        if(indextype == C_PUSH || indextype == C_POP || indextype == C_FUNCTION || indextype == C_CALL) {
            try {
                args2 = Integer.parseInt(segment[2]);

            }

            catch (Exception e) {
                throw new IllegalArgumentException(" Your argument is not an Integer!");
            }
        }
    }

   // Return a constant that represents the command
    public int commandType() {
        if(indextype != -1) {
            return indextype;
        }

        else {
            throw new IllegalStateException("No command!");
        }
    }

    
    public String arg1() {
        if(commandType()!=C_RETURN) {
            return args1;
        }

        else {
            throw new IllegalStateException("Can not get arg1 from a RETURN type command!");
        }
    }

    
    public int arg2() {
        if(commandType() == C_PUSH || commandType() == C_POP || commandType() == C_FUNCTION || commandType() == C_CALL) {
            return args2;
        }

        else {
            throw new IllegalStateException("Can not get arg2!");    
        }
    }

    
    public static String noComments(String str) {
        int pos = str.indexOf("//");
        if(pos != -1) {
            str = str.substring(0, pos);
        }

        return str;
    }
}
