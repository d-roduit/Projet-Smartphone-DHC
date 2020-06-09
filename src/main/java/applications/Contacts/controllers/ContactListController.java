package applications.Contacts.controllers;

import applications.Contacts.Main;
import applications.Contacts.models.Contact;
import applications.Contacts.models.ContactList;
import applications.Contacts.views.ContactAddView;
import applications.Contacts.views.ContactListView;
import applications.Contacts.views.ContactModificationView;
import applications.Contacts.views.ContactView;
import applications.Photos.ThumbnailGenerator;
import applications.Photos.models.PictureModel;
import ch.dhc.ApplicationManager;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import static applications.Photos.Main.GalleryEvent;
import static applications.Photos.Main.GalleryRunningMode;

/**
 * ContactList Controller
 *
 * @author Henrick Neads
 *
 */

public class ContactListController {

    private ContactList contactList;
    private ContactListView contactListView;
    private ContactAddView contactAddView;

    private Main main;

    public ContactListController(Main main, ContactList contactList, ContactListView contactListView, ContactAddView contactAddView) {
        this.main = main;

        this.contactList = contactList;
        this.contactListView = contactListView;
        this.contactAddView = contactAddView;

        initListeners();
    }

    // Update views after adding or removing a contact
    private void updateContactListView(ContactList contactList) {
        main.remove(contactListView);

        contactListView = new ContactListView(contactList);

        main.add(contactListView, String.valueOf(contactListView.hashCode()));

        main.getCardLayout().show(main, String.valueOf(contactListView.hashCode()));

        initListeners();
    }

    // Reseting view in order to remove additional Listeners
    private void updateContactAddView(String imagePath) {
        main.remove(contactAddView);

        contactAddView = new ContactAddView(imagePath);

        main.add(contactAddView, String.valueOf(contactAddView.hashCode()));

        main.getCardLayout().show(main, String.valueOf(contactAddView.hashCode()));

        initListeners();
    }


    // initiate all the listeners
    private void initListeners(){

        this.contactAddView.addSaveContactListener(e-> {
            String firstName = contactAddView.getFirstNameContactTextField().getText();
            String lastName = contactAddView.getLastNameContactTextField().getText();
            String city = contactAddView.getCityContactTextField().getText();
            String phoneNumber = contactAddView.getPhoneNumberContactTextField().getText();
            String email = contactAddView.getEmailContactTextField().getText();

            contactList.addContact(firstName,lastName,city,phoneNumber,email);
            saveContacts();
            contactList.sortContact();
            updateContactListView(contactList);

            updateContactAddView(null);
            contactAddView.getFirstNameContactTextField().setText("");
            contactAddView.getLastNameContactTextField().setText("");
            contactAddView.getCityContactTextField().setText("");
            contactAddView.getPhoneNumberContactTextField().setText("");
            contactAddView.getEmailContactTextField().setText("");

            this.main.getCardLayout().show(this.main,String.valueOf(contactListView.hashCode()));

        });
        this.contactAddView.addReturnToContactList(e -> this.main.getCardLayout().show(this.main, String.valueOf(contactListView.hashCode())));
        this.contactAddView.addPhotoToContactListener(e -> {

            // Ouvrir mon application en mode pictureSelection
            applications.Photos.Main galleryApp = new applications.Photos.Main(GalleryRunningMode.PICTURE_SELECTION);

            galleryApp.addEventListener(GalleryEvent.CLICK_PICTURE_THUMBNAIL, new Consumer<PictureModel>() {
                @Override
                public void accept(PictureModel pictureModel) {
                    String imagePath = pictureModel.getPath();

                    //Revenir à mon application
                    ApplicationManager.getInstance().open(main);

                    updateContactAddView(imagePath);
                }
            });

            ApplicationManager.getInstance().open(galleryApp);
        });

        this.contactListView.addAddContactListener(e->this.main.getCardLayout().show(this.main, String.valueOf(contactAddView.hashCode())));
        this.contactListView.getContactPanelsMap().forEach((contactPanel,contact) ->{

            contactPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    ContactView contactView = new ContactView(contact);
                    contactView.modifyContactListener(event->{
                        ContactModificationView contactModificationView = new ContactModificationView(contact);
                        main.add(contactModificationView,String.valueOf(contactModificationView.hashCode()));
                        main.getCardLayout().show(main,String.valueOf(contactModificationView.hashCode()));

                        contactModificationView.modificationContacteSaveListener(evt->{
                            String firstName = contactModificationView.getFirstNameContactTextField().getText();
                            String lastName = contactModificationView.getLastNameContactTextField().getText();
                            String city = contactModificationView.getCityContactTextField().getText();
                            String phoneNumber = contactModificationView.getPhoneNumberContactTextField().getText();
                            String email = contactModificationView.getEmailContactTextField().getText();

                            modifiyContact(contact,firstName,lastName,city,phoneNumber,email);
                            saveContacts();
                            contactList.sortContact();
                            updateContactListView(contactList);
                            main.getCardLayout().show(main,String.valueOf(contactListView.hashCode()));
                        });

                        contactModificationView.removeContactListener(evt -> {
                            removeContact(contact);
                            saveContacts();
                            contactList.sortContact();
                            updateContactListView(contactList);
                            main.getCardLayout().show(main,String.valueOf(contactListView.hashCode()));
                        });

                        contactModificationView.returnToContactInformationLisetener(evt ->main.getCardLayout().show(main,String.valueOf(contactView.hashCode())));
                    });
                    main.add(contactView,String.valueOf(contactView.hashCode()));
                    main.getCardLayout().show(main,String.valueOf(contactView.hashCode()));

                    contactView.returnToContactListListener(evt ->main.getCardLayout().show(main,String.valueOf(contactListView.hashCode())));
                }
            });
        });
    }

    public void modifiyContact(Contact contact,String firstName,String lastName,String city,String phoneNumber,String email){
        this.contactList.modifyContact(contact,firstName,lastName,city,phoneNumber,email);
    }

    public void removeContact(Contact contact){
        this.contactList.removeContact(contact);
    }

    public void saveContacts() {
        this.contactList.saveContactList();
    }
}
