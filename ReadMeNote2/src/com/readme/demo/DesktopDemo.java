package com.readme.demo;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.readme.client.ReadMeClient;
import com.readme.client.ReadMeConstants;
import com.readme.client.ReadMeException;
import com.readme.data.Note;
import com.readme.data.Notebook;
import com.readme.data.Resource;
import com.readme.data.User;

import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;


/**
 *
 * @author haiwen
 */
public class DesktopDemo {

    // 有道云线上环境
   /* private static final OAuthServiceProvider SERVICE_PROVIDER =
        new OAuthServiceProvider(ReadMeConstants.REQUEST_TOKEN_URL,
                ReadMeConstants.USER_AUTHORIZATION_URL,
                ReadMeConstants.ACCESS_TOKEN_URL);
*/
    //有道云测试环境
    private static final OAuthServiceProvider SANDBOX_SERVICE_PROVIDER =
        new OAuthServiceProvider(ReadMeConstants.SANDBOX_REQUEST_TOKEN_URL,
                ReadMeConstants.SANDBOX_USER_AUTHORIZATION_URL,
                ReadMeConstants.SANDBOX_ACCESS_TOKEN_URL);

    private static final String CONSUMER_KEY = "824cf448318658ea7e1738d90f55bbab";
    private static final String CONSUMER_SECRET = "087544ef5fb142fbb50bf5a790b92981";

//    private static final String CONSUMER_KEY = "7bac56b6e961a834313726a060c69b24";
//    private static final String CONSUMER_SECRET = "b17ce2bf8e52bd4468300cda365b2bb3";
    // sandbox consumer
    private static final OAuthConsumer CONSUMER = new OAuthConsumer(null,
            CONSUMER_KEY, CONSUMER_SECRET, SANDBOX_SERVICE_PROVIDER);

    private static ReadMeClient client = new ReadMeClient(CONSUMER);

    public static void main(String[] args) throws Exception {
        
       

        // get the user information
        User user = getUser();
        System.out.println("Get user information: ");
        System.out.println(user);
        List<Notebook> notebooks = getAllNotebooks();
        System.out.println(notebooks);
        String notebook = createNotebook("New_Notebook", null);
        System.out.println("New Notebook is create " + notebook);
        // create a note under this notebook
        Note note = createNote(notebooks.get(0).getPath());
        System.out.println("New Note is create " + note);
        // list the new notebook, there should be one note
        List<String> notes = listNotes(notebook);
        System.out.println("Notes under the new notebook " + notes);
        // move the new note to another notebook
        Notebook anotherNotebook = notebooks.get(0);
        String newNotePath = moveNote(note.getPath(), anotherNotebook.getPath());
        System.out.println("Note " + note.getPath() + " is moved to " + newNotePath);
        // get the note with new note path
        note = getNote(newNotePath);
        System.out.println("Get the note with moved path " + newNotePath);
        // upload a image resource as well as a attachment resource
        Resource resource1 = uploadResource(new File("conf", "access_token"));
        System.out.println("Upload attachment resource");
        Resource resource2 = uploadResource(downloadImage());
        System.out.println("Upload image resource");
        // modify the content of the note to include these two resource
        String content = resource1.toResourceTag() + "<br>" + resource2.toResourceTag();
        System.out.println(content);
        note.setContent(content);
        updateNote(note);
        System.out.println("Upload the note to include the two resources");
        // download the resource
        byte[] bytes = downloadResource(resource1.getUrl());
        FileOutputStream output = new FileOutputStream(new File("download-file"));
        output.write(bytes);
        output.close();
        // delete the note
        deleteNote(newNotePath);
        System.out.println("Delete the note " + newNotePath);
        // delete the notebook
        deleteNotebook(notebook);
        System.out.println("Delete the notebook " + notebook);
        // create a note under the application's default notebook
        createNote(null);
        System.out.println("Create a note under application's default notebook");
    }

    private static File downloadImage() throws IOException {
        URL url = new URL("http://note.youdao.com/styles/images/case-1.png");
        byte[] bytes = toBytes(url.openStream());
        File image = new File("case-1.png");
        FileOutputStream output = new FileOutputStream(image);
        output.write(bytes);
        output.close();
        return image;
    }

   

    private static User getUser() throws Exception {
        User user = null;
        try {
            user = client.getUser();
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            } else {
                throw e;
            }
        }
        return user;
    }

    private static List<Notebook> getAllNotebooks() throws Exception {
        List<Notebook> notebooks = new ArrayList<Notebook>();
        try {
            notebooks = client.getAllNotebooks();
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            } else {
                throw e;
            }
        }
        return notebooks;
    }

    private static List<String> listNotes(final String notebook) throws Exception {
        List<String> notes = new ArrayList<String>();
        try {
            notes = client.listNotes(notebook);
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            } else {
                throw e;
            }
        }
        return notes;
    }

    private static String createNotebook(final String name, final String group) throws Exception {
        String notebook = null;
        try {
            notebook = client.createNotebook(name, group);
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            } else {
                throw e;
            }
        }
        return notebook;
    }

    private static void deleteNotebook(final String notebook) throws Exception {
        try {
            client.deletedNotebook(notebook);
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            } else {
                throw e;
            }
        }
    }

    private static Note createNote(final String notebookPath) throws Exception {
        final Note note = new Note();
        note.setAuthor("");
        note.setSize(100);
        note.setSource("");
        note.setTitle("测试");
        note.setContent("测试");
        try {
            return client.createNote(notebookPath, note);
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            	return null;
            } else {
                throw e;
            }
        }
    }

    private static Note getNote(final String notePath) throws Exception {
        try {
            return client.getNote(notePath);
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            	return null;
            } else {
                throw e;
            }
        }
    }

    private static void updateNote(final Note note)
            throws Exception {
        try {
            client.updateNote(note);
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            	
            } else {
                throw e;
            }
        }
    }

    private static String moveNote(final String notePath, final String destNotebookPath)
            throws Exception {
        try {
            return client.moveNote(notePath, destNotebookPath);
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            	return null;
            } else {
                throw e;
            }
        }
    }

    private static void deleteNote(final String notePath) throws Exception {
        try {
            client.deleteNote(notePath);
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            } else {
                throw e;
            }
        }
    }

    private static Resource uploadResource(final File resource) throws Exception {
        try {
            return client.uploadResource(resource);
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            	return null;
            } else {
                throw e;
            }
        }
    }

    private static byte[] downloadResource(final String url) throws Exception {
        InputStream body = null;
        try {
            body = client.downloadResource(url);
        } catch (ReadMeException e) {
            if (e.getErrorCode() == 307 || e.getErrorCode() == 1017) {
            	//Toast.makeText(MyNoteActivity.this, "-----我错了",Toast.LENGTH_SHORT).show();
            } else {
                throw e;
            }
        }
        return toBytes(body);
    }

    private static byte[] toBytes(InputStream input) throws IOException {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int n = -1;
            while ((n = input.read(buffer)) != -1) {
                bytes.write(buffer, 0, n);
            }
            bytes.close();
            return bytes.toByteArray();
        } finally {
            input.close();
        }
    }
}
