package org.Proiect.Servicii;

import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Domain.Angajati.Utilizator;

public interface IEchipaFactory {
    /**
     * Creează o echipă nouă cu un lider specificat.
     *
     * @param numeEchipa Numele echipei.
     * @param lider Liderul echipei.
     * @return O instanță de Echipa configurată.
     */
    Echipa creeazaEchipa(String numeEchipa, Utilizator lider);

    /**
     * Creează o echipă fără un lider (opțional pentru fluxuri de lucru speciale).
     *
     * @param numeEchipa Numele echipei.
     * @return O instanță de Echipa fără lider.
     */
    Echipa creeazaEchipaFaraLider(String numeEchipa);

    /**
     * Creează o echipă arhivată.
     *
     * @param numeEchipa Numele echipei.
     * @param lider Liderul echipei.
     * @return O instanță de Echipa marcată ca arhivată.
     */
    Echipa creeazaEchipaArhivata(String numeEchipa, Utilizator lider);
}
