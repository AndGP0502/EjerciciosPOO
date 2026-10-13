    public class Usuario {

        private String username;
        private String correo;
        private boolean activo;


        //username validaciones y obtención

        public String getUsername(){

            return username;

        }


        public void setUsername(String username) {

            if(username != null && !username.isBlank()) {

                this.username = username;

                System.out.println("El usuario es: " + username + ", bienvenido");
            }

            else {

                System.out.println("Debes escribir algo");
            }
        }

        //fin de username

        //Inicio de activo

        public boolean isActivo(){

            return activo;
        }

        public void setActivo(boolean activo){

            if(activo && username == null){

                System.out.println("No hay usuarios");
                System.out.println("Estado: Inactivo");

            } else {

                this.activo = activo;

                System.out.println("El estado es: Activo ");
            }
        }

        public String getCorreo (){

            return correo;
        }

        public void setCorreo(String correo) {

            if(correo != null && !correo.isBlank() && correo.contains("@")){

                this.correo = correo;

                System.out.println("Excelente, correo validado: " + correo);
            } else {

                System.out.println("Ingresa un correo válido");
            }
        }

    }
