package tp4.model;

public interface IAlertOutput {

    void saveAlert(String type, double releve, double threshold, Date timestamp, String address);
}
