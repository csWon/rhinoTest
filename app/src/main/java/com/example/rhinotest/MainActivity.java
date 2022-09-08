package com.example.rhinotest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private ContextFactory f = new ContextFactory();
    private Context rhino = f.enterContext();
    private Scriptable _scope = rhino.initStandardObjects();
    private StringBuilder jsCode = new StringBuilder();




//    Context cx = Context.enter();
//    Scriptable scope = cx.initStandardObjects();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        rhino.setOptimizationLevel(-1);

        String f1 = "function f1(a,b)\n" +
                "{\n" +
                "return a+b"+
                "}";

        String f2 = "function f2(a)\n" +
                "{\n" +
                "return a+10"+
                "}";

        String f3 = "var f = function(){\n" +
                "\treturn \"textFunction\";\n" +
                "}";
        String code = "var name='nc',global_a = 'jill'; " + "\n"+
                "function myfunc(b) { " + "\n"+
                "var local_a = 1;" + "\n"+
                "global_a = 'jack';" + "\n"+
                " return b;" + "\n"+
                "}";

        addFunction(f1,"ScriptAPI");
        addFunction(f2,"ScriptAPI");
        addFunction(f3,"ScriptAPI");

        Test test = new Test();
        Context cx = Context.enter();
        Log.d("Tag",test.GetStr());

//        String evaluationScript = "test.str;";
        String evaluationScript = "test.SetDefaultStr();";
        String es1 = "var s = 'testStr'";
        String es2 = "function myFunction(str) {\n" +
                "  return str + '_js';\n" +
                "}\n";
        String es3 = "var r = myFunction(s);\n";

        try {
            // Initialize the standard objects (Object, Function, etc.). This must be done before scripts can be
            // executed. The null parameter tells initStandardObjects
            // to create and return a scope object that we use
            // in later calls.
            Scriptable scope = cx.initStandardObjects();

            // Pass the Stock Java object to the JavaScript context
            Object wrappedStock = Context.javaToJS(test, scope);
            ScriptableObject.putProperty(scope, "test", wrappedStock);

            // Execute the script
//            cx.evaluateString(scope, evaluationScript, "EvaluationScript", 1, null);
            cx.evaluateString(scope, es1, "test", 1, null);
            cx.evaluateString(scope, es2, "test", 1, null);
            cx.evaluateString(scope, es3, "test", 1, null);
            Object result = scope.get("r", scope);

            Log.d("Tag",test.GetStr());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Exit the Context. This removes the association between the Context and the current thread and is an
            // essential cleanup action. There should be a call to exit for every call to enter.
            Context.exit();
        }

//        addFunction(code);
        //String a = GetFramesetCode();
//        addFunction(a);
        //
//        Object p[] =  new Object[] {"p1", "p2"};
//        callFunction("f1", p);
//
//        Object p2[] = new Object[]{19};
//        callFunction("f2", p2);

        Object p3[] = new Object[]{"aa", "bb"};
        callFunction("f1", p3);
//
//
//        Object p3[] = new Object[]{"self.frames['mainFrame']", "self", "sTitle"};
//
//        Object p3[] = new Object[]{self.frames['mainFrame'], self, sTitle};
//        callFunction("oFramesetTitleController.start", p3);
//
//        oFramesetTitleController.start(self.frames['mainFrame'], self, sTitle);
    }

    public void addFunction(String code, String srcName){
        jsCode.append(code);
        rhino.evaluateString(_scope, jsCode.toString(), srcName, 1, null);

    }

    public void clearCode(){
        jsCode = new StringBuilder();
    }

    public String getCode(){
        return jsCode.toString();
    }

    public void callFunction(String funcName, Object[] p) {
        try {
            Object obj = _scope.get(funcName, _scope);

            if (obj instanceof Function) {
                Function func = (Function) obj;
                Object jsResult = func.call(rhino, _scope, _scope, p);
                Log.d("Tag", jsResult.toString());
            }
            else{
                Log.d("Tag", funcName +" is not function");
            }
        }
        catch (Exception e) {
            Log.e("exc",e.toString());
        }
    }
//    public String test() {
//        writeFile("jsTest2.txt", "test code");
//        String s = readFile
//        return "";
//    }
//
//    public String GetFramesetCode()
//    {
//        ///Users/csWon/StudioProjects/rhinoTest/framesetCode.txt
//        File file = new File("/Users/csWon/framesetCode.txt");
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
//            return line;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    private void writeFile(String fName, String msg)
//    {
//        try{
//            OutputStreamWriter osw = new OutputStreamWriter(openFileOutput(fName, 0));
//            osw.write(msg);
//            osw.close();
//        }
//        catch (FileNotFoundException e)
//        {
//
//        }
//        catch (IOException e)
//        {
//
//        }
//
//    }
}

class Test{
    int a;
    String str;
    public Test() {
        a = 10;
        str = "from Java";
    }

    public int GetNum() {
        return a;
    }

    public void SetNum(int n){
        a = n;
    }

    public void SetStr(String str){
        this.str = str;
    }

    void SetDefaultStr(){
        this.str = "from JS default";
    }

    public String GetStr(){
        return this.str;
    }
}