package hopital.test;

import hopital.Hopital;
import windows.medecin.FrameCreateConsultation;

public class TestFrameAddConsultation {

    public static void main(String[] args) {
        Hopital.loadingHopitalPersonnel();
        new FrameCreateConsultation();
    }

}
