package org.mvvm.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mvvm.model.Member;
import org.mvvm.model.repository.MemberRepo;
import org.mvvm.view.FormV;
import org.mvvm.viewmodel.commands.Command;
import org.mvvm.viewmodel.commands.MemberCommands;

import javax.xml.transform.Source;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemberVM implements InterfaceVM{
    private List<Member> members;
    private Member crtMember = new Member();
    public ObjectProperty<ObservableList<List<String>>> allMemberAttributes = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    public final ObjectProperty<List<String>> crtMemberAttributes = new SimpleObjectProperty<>();

    public final StringProperty message = new SimpleStringProperty("No messages");
    public StringProperty crtFilter = new SimpleStringProperty("No filter");
    private final MemberRepo memberRepo;

    public final MemberCommands addCommand;
    public final MemberCommands deleteCommand;
    public final MemberCommands updateCommand;
    public final MemberCommands filterCommand;
    public final MemberCommands viewImageCommand;


    public final MemberCommands commitNameCommand;
    public final MemberCommands commitBirthdateCommand;
    public final MemberCommands commitTypeCommand;
    public final MemberCommands commitImageURLCommand;

    public MemberVM() {
        this.addCommand = new MemberCommands((v) -> addItem());
        this.deleteCommand = new MemberCommands((v) -> deleteItem());
        this.updateCommand = new MemberCommands((v) -> updateItem());
        this.filterCommand = new MemberCommands((v) -> listMembers());
        this.viewImageCommand = new MemberCommands((v) -> quickImageForm());

        this.commitNameCommand = new MemberCommands((v) -> commitMemberField(1, v));
        this.commitBirthdateCommand = new MemberCommands((v) -> commitMemberField(2, v));
        this.commitTypeCommand = new MemberCommands((v) -> commitMemberField(3, v));
        this.commitImageURLCommand = new MemberCommands((v) -> commitMemberField(4, v));

        this.memberRepo = new MemberRepo();

        listMembers();
    }

    public void updateItem() {
        try{
            reconstructCurrentMember();
            memberRepo.updateMember(crtMember);
            listMembers();
        }catch (SQLException e){
            message.set("Failed to update member");
        }
    }

    public void deleteItem() {
        try{
            reconstructCurrentMember();
            memberRepo.deleteMember(crtMember.getId());
            listMembers();
        }catch (SQLException e){
            message.set("Failed to delete member");
        }
    }

    public void addItem() {
        try{
            memberRepo.addMember(new Member());
            crtFilter.set("No filter"); //resets filters automatically after adding a new member
            listMembers();
        }catch (SQLException e){
            message.set("Failed to add member");
        }
    }

    private void listMembers() {
        try {
            if(Objects.equals(crtFilter.get(), "No filter") || Objects.equals(crtFilter.get(), "Filter by"))
                members = new ArrayList<>(memberRepo.getAllMembers(null));
            else
                members = new ArrayList<>(memberRepo.getAllMembers(crtFilter.get()));
            breakdownAllMembers();
        } catch (SQLException e) {
            message.set("Failed to load members");
        }
    }
    public void quickImageForm(){
        reconstructCurrentMember();
        //Convert Windows path to JavaFX-compatible URI
        String imageURL = "file:///" + crtMember.getImage().replace("\\", "/");

        FormV form = new FormV(this);
        form.initializeItemImageForm(imageURL, crtMember.getName());
    }

    public void breakdownAllMembers() {
        allMemberAttributes.get().clear();
        for (Member member : members) {
            List<String> attributes = new ArrayList<>();

            attributes.add(String.valueOf(member.getId()));
            attributes.add(member.getName());
            attributes.add(String.valueOf(member.getBirthDate()));
            attributes.add(String.valueOf(member.getBaseType()));
            attributes.add(String.valueOf(member.getImage()));

            allMemberAttributes.get().add(attributes);
        }
    }

    private void reconstructCurrentMember(){
        try {
            crtMember.setId(Integer.parseInt(crtMemberAttributes.get().get(0)));
            crtMember.setName(crtMemberAttributes.get().get(1));
            crtMember.setBirthDate(crtMemberAttributes.get().get(2));
            crtMember.setBaseType(crtMemberAttributes.get().get(3));
            crtMember.setImage(crtMemberAttributes.get().get(4));
            message.set("Member " + crtMember.getId() + " processed");
        }catch(NullPointerException e){
            message.set("No member selected");
        }
    }

    public void commitMemberField(int fieldIndex, String newValue) {
        if (crtMemberAttributes.get() != null) {
            crtMemberAttributes.get().set(fieldIndex, newValue);
        }
    }
}
