/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.entities;

import java.util.Objects;

/**
 *
 * @author Andrej Halaj
 * @author Matej Majdis
 */
public class Mission {

    private Long id;
    private String codeName;
    private String objective;
    private String location;
    private String notes;

    public Mission() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 53 * hash + Objects.hashCode(this.codeName);
        hash = 53 * hash + Objects.hashCode(this.objective);
        hash = 53 * hash + Objects.hashCode(this.location);
        hash = 53 * hash + Objects.hashCode(this.notes);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mission other = (Mission) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.codeName, other.codeName)) {
            return false;
        }
        if (!Objects.equals(this.objective, other.objective)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        return Objects.equals(this.notes, other.notes);
    }

    @Override
    public String toString() {
        String ret = "Mission \"" + codeName + "\" [objective= " + objective + ", location= " + location;
        if (notes != null) {
            ret += ", notes= " + notes;
        }
        ret += "]";

        return ret;
    }

}
