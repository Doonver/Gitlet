package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import static gitlet.Utils.*;

public class Stage implements Serializable {
    private HashMap<String, String> addStage;
    private HashMap<String, String> rmStage;
     /** Constructor function for our stage */
    public Stage() {
        addStage = new HashMap<String, String>();
        rmStage = new HashMap<String, String>();
        writeObject(Repository.STAGE_OBJECT, this);
    }

    /** Puts the hash code into the addStage HashMap, creates a blob, and then writes
     * the object into the STAGE_OBJECT to store the data for future adds */

    public void putAddStage(String fileName, String hash, File filePointer) {
        rmStage.remove(fileName);
        addStage.put(fileName, hash);
        Blob b = new Blob(filePointer);
        writeObject(Repository.STAGE_OBJECT, this);
    }

    public void putRmStage(String fileName, String hash) {
        String branchString = Repository.getCurrBranch().getBranchName();
        Commit head = Repository.getHeadOfBranch(branchString);
        //if add stage contains the removing file, remove it
        if (addStage.containsKey(fileName)) {
            addStage.remove(fileName);
            //if the file is tracked by the previous commit, remove the file from CWD
        }
        if (head.getCommitHash().containsKey(fileName)) {
            rmStage.put(fileName, hash);
            restrictedDelete(join(Repository.CWD, fileName));
        }
        writeObject(Repository.STAGE_OBJECT, this);
    }


    /** These get functions allow access to the private variables of the Stage class */
    public HashMap<String, String> getAddStage() {
        return this.addStage;
    }

    public HashMap<String, String> getRmStage() {
        return this.rmStage;
    }
}
