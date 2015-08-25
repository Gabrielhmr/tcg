package br.uece.lotus.tcg;

import br.uece.lotus.Component;
import br.uece.lotus.Transition;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.tcg.utils.DebugLog;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

public class TcgPlugin extends Plugin implements Tcg {

    private ProjectExplorer mProjectExplorer;

    public static enum Mode {
        Statistical, Functional, Exception
    };

    private Mode windowMode;

    @Override
    public void show(Component c, boolean editable) {
        
        URL location;
        location = getClass().getResource("gui/MainWindow.fxml");
        FXMLLoader loader = new FXMLLoader();

        ResourceBundle bundle = new ResourceBundle() {
            Component mComponent = c;
            boolean mStatistical = windowMode == Mode.Statistical;

            @Override
            protected Object handleGetObject(String key){
                if (key == "component"){
                    return mComponent;
                }

                if (key == "statisticalmode"){
                    return mStatistical;
                }

                return null;
            }

            @Override
            public Enumeration<String> getKeys(){
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        try {
            loader.setClassLoader(getClass().getClassLoader());
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setResources(bundle);
            Parent root = (Parent) loader.load(location.openStream());

            int id = mUserInterface.getCenterPanel().newTab(c.getName() + " - [TCG]", root, true);
            mUserInterface.getCenterPanel().showTab(id);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private Runnable mExceptionTests = () -> {

        Component c = mProjectExplorer.getSelectedComponent();

        try {
            if (c == null){
                JOptionPane.showMessageDialog(null, "Select a component!");
                return;
            }

            windowMode = Mode.Exception;

            show(c.clone(), true);

        }catch (CloneNotSupportedException ex){
            Logger.getLogger(Tcg.class.getName()).log(Level.SEVERE, null, ex);

        }

    };

    private Runnable mFunctionalTests = () -> {

        Component c = mProjectExplorer.getSelectedComponent();

        try{
            if (c == null){
                JOptionPane.showMessageDialog(null, "Select a component!");
                return;
            }

            windowMode = Mode.Functional;

            show(c.clone(), true);

        }catch (CloneNotSupportedException ex){
            Logger.getLogger(Tcg.class.getName()).log(Level.SEVERE, null, ex);
        }

    };

    private Runnable mStatisticalTests = () -> {
        
        DebugLog.printLog("TCG - Statistical Tests Menu Pressed");
        Component c = mProjectExplorer.getSelectedComponent();

        try {
            if (c == null){
                JOptionPane.showMessageDialog(null, "Select a component!");
                return;
            }
            windowMode = Mode.Statistical;

            for (Transition t : c.getTransitions()){
                if (t.getProbability() == null){
                    JOptionPane.showMessageDialog(null, "There are no probability setted for some transitions!");
                    return;
                }
            }

            show(c.clone(), true);
        }catch (CloneNotSupportedException ex){

            Logger.getLogger(Tcg.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    private Runnable mAboutRunnable = () -> {
        
        DebugLog.printLog("TCG - About Pressed");
        URL location = getClass().getResource("gui/AboutWindow.fxml");
        FXMLLoader loader = new FXMLLoader();

        loader.setClassLoader(getClass().getClassLoader());
        loader.setLocation(location);
        loader.setBuilderFactory(new JavaFXBuilderFactory());

        try {
            Parent root = (Parent) loader.load(location.openStream());

            Scene scene = new Scene(root);
            Stage window = new Stage(StageStyle.UTILITY);
            window.initModality(Modality.APPLICATION_MODAL);

            window.setResizable(false);
            window.setMinWidth(810);
            window.setMinHeight(410);

            window.setScene(scene);
            window.showAndWait();
            
        }catch (IOException e) {

        }

    };

    private UserInterface mUserInterface;

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception{

        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);

        mUserInterface.getMainMenu().newItem("TCG/Functional").setWeight(1).setAction(mFunctionalTests).create();
        mUserInterface.getMainMenu().newItem("TCG/Statistical").setWeight(1).setAction(mStatisticalTests).create();
        mUserInterface.getMainMenu().newItem("TCG/About").setWeight(1).setAction(mAboutRunnable).create();

//        mUserInterface.getMainMenu().addItem(Integer.MAX_VALUE - 1, "TCG/Exception", mExceptionTests);
    }

}
