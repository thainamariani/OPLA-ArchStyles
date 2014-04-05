/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import arquitetura.representation.Architecture;

/**
 *
 * @author Thainá
 */
public class ArchitectureRepository {

    public static Architecture CURRENT_ARCHITECTURE = null;

    private ArchitectureRepository() {
    }

    public static Architecture getCurrentArchitecture() {
        return CURRENT_ARCHITECTURE;
    }

    public static void setCurrentArchitecture(Architecture architecture) {
        CURRENT_ARCHITECTURE = architecture;
    }
}
