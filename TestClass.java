import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TestClass extends ModuleInterface {
    public TestClass()
    {
        setTitle("Test Window");
        setSize(300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    public String getMenu()
    {
        return "Test Class";
    }
}
