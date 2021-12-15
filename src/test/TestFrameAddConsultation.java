package test;

import hopital.Hopital;
import windows.medecin.FrameConsultation;

public class TestFrameAddConsultation {

    public static void main(String[] args) {
        Hopital.loadingMedecin();
        new FrameConsultation();
    }

}
