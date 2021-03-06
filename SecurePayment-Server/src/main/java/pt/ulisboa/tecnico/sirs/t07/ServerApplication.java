package pt.ulisboa.tecnico.sirs.t07;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.sirs.t07.presentation.UDPConnectionManager;

import java.io.IOException;


/**
 * Created by trosado on 03/11/16.
 */
public class ServerApplication {

    public static void main(String[] args) throws IOException {
       /*
        * CustomerData cd = new CustomerData();
		System.out.print(cd.ibanExists("NL73INGB0698363980"));
		cd.close();

		AccountData contaD = new AccountData();

		get("/contas", (req, res) -> contaD.getContas());

		get("/conta/:iban", (req, res) -> {

			String iban = req.params(":iban");
			Vector<Float> saldo = contaD.getBalanceFromIBAN(iban);
			return saldo;


		});
 */
            /**
            * TODO
            *  - Threads (cada cliente)
            *  - JDBC interface
            *  - enviar resposta até receber confirmação
            *  - tamanho maximo UDP
            * */


            /**
             * TODO
             *  Operações
             *  - Transferência
             *  - Histórico
             *  - Saldo
             *
             *  Respostas
             *  - Saldo Insuficiente
             *  - Operação repetida
             *  - Confirmação
             *  - Mensagem de Bloqueio
             */


        /**
         * 1 ~ Espera de conexões (porta)
         * 2 - Se tiver conexão muda para serviço
         */

        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");

        UDPConnectionManager cm = new UDPConnectionManager();
        cm.execute();











/*
        CustomerData cd = new CustomerData();
        System.out.print(cd.ibanExists("NL73INGB0698363980"));
        cd.close();
*/
    }
}

