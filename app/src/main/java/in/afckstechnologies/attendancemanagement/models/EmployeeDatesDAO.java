package in.afckstechnologies.attendancemanagement.models;

/**
 * Created by admin on 3/7/2017.
 */

public class EmployeeDatesDAO {
    String day = "";
    String dates = "";

    public EmployeeDatesDAO(String day, String dates) {
        this.day = day;
        this.dates = dates;
    }

    public EmployeeDatesDAO() {

    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    @Override
    public String toString() {
        return dates;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EmployeeDatesDAO) {
            EmployeeDatesDAO c = (EmployeeDatesDAO) obj;
            if (c.getDates().equals(dates) && c.getDay() == day) return true;
        }

        return false;
    }
}
