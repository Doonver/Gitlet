package gitlet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;
import java.util.HashMap;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  This commit object stores all the things a commit needs to store
 *  while also having commands that the commit method in Repository can
 *  call on
 *
 *  @author Denver Nguyen
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** When the Commit was created */
    private String timestamp;
    /** The parent of the Commit in SHA-1 code*/
    private String parent1;
    /** Used for the 2 parents for merge */
    private String parent2;
    /** HashMap used to associate the name of the file with a file */
    private HashMap<String, String> commitHash;
    /** SHA-1 code of this commit */
    private String shaID;


    /** Date without parameter gets time when it was called.
     *
     * Commit constructor starts the Commit with a message, parent, timestamp,
     * and a commitHash that is empty;
     */
    public Commit(String message, String parent) {
        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        this.message = message;
        this.parent1 = parent;
        this.commitHash = new HashMap<String, String>();
        if (parent == null) {
            this.timestamp = simpleDateFormat.format(new Date(0));
        } else {
            this.timestamp = simpleDateFormat.format(new Date());
        }
    }


    /** Gets the stage that was added and replaces the commitHash with it to keep track
     * of the file names associated with each blob
     */
    public void addRemoveFilesToCommit(Stage stage) {
        HashMap<String, String> stageAdd = stage.getAddStage();
        HashMap<String, String> stageRm = stage.getRmStage();
        String currBranchName = Repository.getCurrBranch().getBranchName();
        HashMap<String, String> oldCH = Repository.getHeadOfBranch(currBranchName).getCommitHash();
        for (String names : stageAdd.keySet()) {
            String shaIDs = stageAdd.get(names);
            oldCH.put(names, shaIDs);
        }
        for (String names : stageRm.keySet()) {
            oldCH.remove(names);
        }
        this.commitHash = oldCH;
    }

    public void assignParent2(String parent) {
        this.parent2 = parent;
    }

    public void changeShaID(String shaIDs) {
        this.shaID = shaIDs;
    }

    /** All of these functions allow access to the private instance variables */

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getMessage() {
        return this.message;
    }

    public String getParent1() {
        return this.parent1;
    }

    public String getParent2() {
        return this.parent2;
    }

    public HashMap<String, String> getCommitHash() {
        return this.commitHash;
    }

    public String getShaID() {
        return this.shaID;
    }
}
