package com.backend.cyberbytes.service;

import com.backend.cyberbytes.model.SecurityCode;
import com.backend.cyberbytes.repository.SecurityCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class SecurityCodeService {

    //Dependencias
    @Autowired
    SecurityCodeRepository repository;

    @Autowired
    EmailService emailServices;

    /*
     * Gera um código numerico de 6 digitos
     * */
    public int genCode(){
        Random gen = new Random();
        //Gera um numero aleatorio de 000000 a 999999
        return gen.nextInt(1000000);
    }

    //Chamar isso quando a o codigo for gerado
    /*
     * Função respoosnssável por gerar o código de verificação, salvando o no banco de dados para verificação futura
     * e envio do código por um email html para o usuário
     * */
    public void saveCode(String email) {
        // Busca um código existente pelo email
        Optional<SecurityCode> codeOptional = Optional.ofNullable(repository.findByEmail(email));
        // Se existir, deleta o código anterior
        codeOptional.ifPresent(securityCode -> repository.deleteById(securityCode.getId()));

        //Informações para salvamento do código
        int code = genCode();
        LocalDateTime now = LocalDateTime.now();
        SecurityCode cod = new SecurityCode(email, code, now);
        repository.save(cod);

        //Envio do email em formato html
        emailServices.sendCodeEmail(
                email,
                "Verificação de email",
                String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    background-color: white;
                                    background-image: linear-gradient( 180deg, #90aeff, #60efb8, #cefc86);
                                    color: black;
                                    padding: 0px;
                                }
                                .email-container {
                                    background-color: #f9f7f7;
                                    border-radius: 10px;
                                    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                                    padding: 20px;
                                    text-align: center;
                                    width: 60vw;
                                    margin: auto;
                                    color: #000;
                                    margin-top: 30px;
                                    margin-bottom: 50px;
                                }
                                #navbar{
                                    background-color: rgba(0, 0, 0, 0);
                                    display: block;
                                    width: 100vw;
                                    text-align: center;
                                    padding-top: 10px;
                                    padding-bottom: 10px;
                                }
                        
                                .email-container h1{
                                    color: red;
                                }
                        
                            </style>
                        </head>
                        <body>
                            <nav id="navbar">
                                <h1>CyberBytes</h1>
                            </nav>
                            <div class="email-container">
                                <h4>Verificação de email</h4>
                                <p>
                                    Prezado usuário,<br>
                        
                                    O e-mail %s está sendo cadastrado em nosso site <b>CyberBytes</b>. <br>
                                    Aqui está o seu código de verificação:
                                    <br>
                                    <h1>%d.</h1>
                                    <br>
                        
                                    Se você não realizou essa solicitação, por favor, ignore esta mensagem e não compartilhe este código.
                        
                                    Agradecemos sua atenção.
                                    Atenciosamente,<br>
                                    <b>Equipe CyberBytes</b>
                                </p>
                            </div>
                        </body>
                        </html>""", email, code)//Atributos inseridos na string de multiplas linhas

        );
    }

    //Método para enviar um código para usuário alterar sua senha
    public void sendRecoveryCode(String email){
        // Busca um código existente pelo email
        Optional<SecurityCode> codeOptional = Optional.ofNullable(repository.findByEmail(email));
        // Se existir, deleta o código anterior
        codeOptional.ifPresent(securityCode -> repository.deleteById(securityCode.getId()));

        //Informações para salvamento do código
        int code = genCode();
        LocalDateTime now = LocalDateTime.now();
        SecurityCode cod = new SecurityCode(email, code, now);
        repository.save(cod);

        //Envio do email em formato html
        emailServices.sendCodeEmail(
                email,
                "Redefinição de Senha",
                String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    background-color: white;
                                    background-image: linear-gradient( 180deg, #90aeff, #60efb8, #cefc86);
                                    color: black;
                                    padding: 0px;
                                }
                                .email-container {
                                    background-color: #f9f7f7;
                                    border-radius: 10px;
                                    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                                    padding: 20px;
                                    text-align: center;
                                    width: 60vw;
                                    margin: auto;
                                    color: #000;
                                    margin-top: 30px;
                                    margin-bottom: 50px;
                                }
                                #navbar{
                                    background-color: rgba(0, 0, 0, 0);
                                    display: block;
                                    width: 100vw;
                                    text-align: center;
                                    padding-top: 10px;
                                    padding-bottom: 10px;
                                }
                        
                                .email-container h1{
                                    color: red;
                                }
                        
                            </style>
                        </head>
                        <body>
                            <nav id="navbar">
                                <h1>CyberBytes</h1>
                            </nav>
                            <div class="email-container">
                                <h4>Alterar Senha</h4>
                                <p>
                                    Prezado usuário,<br>
                        
                                    Recebemos uma solicitação para redefinir a senha da sua conta associada ao e-mail <b>%s</b> no site <b>CyberBytes</b> <br>
                                    Aqui está o seu código de verificação:
                                    <br>
                                    <h1>%d.</h1>
                                    <br>
                        
                                    Se você não realizou essa solicitação, por favor, ignore esta mensagem e não compartilhe este código.
                        
                                    Agradecemos sua atenção.
                                    Atenciosamente,<br>
                                    <b>Equipe CyberBytes</b>
                                </p>
                            </div>
                        </body>
                        </html>""", email, code)//Atributos inseridos na string de multiplas linhas

        );
    }

    /*
     * Verifica se o código salvo no banco de dados e o informado pelo o usuário são os mesmos
     * */
    public boolean verifyCode(String email, int attempt){

        Optional<SecurityCode> codeOptional = Optional.ofNullable(repository.findByEmail(email));

        if (codeOptional.isEmpty()) {
            System.out.println("DEBUG: Código não encontrado para o email: " + email);
            return false;
        }

        SecurityCode securityCode = codeOptional.get();

        Duration duration = Duration.between(securityCode.getCreatedAt(), LocalDateTime.now());

        if (securityCode.getCode() == attempt && duration.toHours() <= 24) {
            deleteCode(securityCode);
            System.out.println("DEBUG: Código válido e não expirado para o email: " + email);
            return true;
        } else {
            System.out.println("DEBUG: Código inválido ou expirado para o email: " + email + ", Código fornecido: " + attempt + ", Horas desde a criação: " + duration.toHours());
            return false;
        }
    }

    /*
     * Função responsável por deletar o código no banco de dados
     * */
    public void deleteCode(SecurityCode securityCode){
        repository.deleteById(securityCode.getId());
    }
}
