/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.ClassDAO;
import java.util.List;
import model.Class;

/**
 *
 * @author Do Duan
 */
public class ClassService {
    private ClassDAO classDAO;

    public Class searchClassByID(int id) {
        classDAO = new ClassDAO();
        if (classDAO.getClassByID(id) == null) {
            throw new IllegalArgumentException("Class not found");
        }
        return classDAO.getClassByID(id);
    }

    public boolean addClass(int id, String name,String detail,int status,int semesterID, int groupID) {
        
        Class c = new Class(id, name, detail, status,semesterID, groupID);
        classDAO = new ClassDAO();
        if (classDAO.createClass(c) != 0) {
            return true;
        }
        return false;
    }
    
    public List<Class> getClassList(int userID, int settingID, int groupID, String searchString){
        classDAO = new ClassDAO();
        return classDAO.searchClass(userID, settingID, groupID, searchString);
    }
}
