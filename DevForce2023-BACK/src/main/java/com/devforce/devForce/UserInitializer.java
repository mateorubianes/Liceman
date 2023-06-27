package com.devforce.devForce;

import com.devforce.devForce.model.entity.*;
import com.devforce.devForce.model.enums.ERole;
import com.devforce.devForce.repository.LicenciaRepository;
import com.devforce.devForce.repository.RoleRepository;
import com.devforce.devForce.repository.SolicitudRepository;
import com.devforce.devForce.repository.UsuarioRepository;
import com.devforce.devForce.service.UsuarioService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class UserInitializer implements CommandLineRunner {

    @Value("${sample.data}")
    private Boolean datosDePrueba;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private LicenciaRepository licenciaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {

        if (datosDePrueba) {

            Role userRole = new Role(ERole.ROLE_USUARIO);
            Role mentorRole = new Role(ERole.ROLE_MENTOR);
            Role adminRole = new Role(ERole.ROLE_ADMIN);
            roleRepository.save(userRole);
            roleRepository.save(mentorRole);
            roleRepository.save(adminRole);

            Set<Role> userRoles = new HashSet();
            Role r = roleRepository.findByName(ERole.ROLE_USUARIO).orElse(null);
            userRoles.add(r);

            Set<Role> mentorRoles = new HashSet();
            Role m = roleRepository.findByName(ERole.ROLE_MENTOR).orElse(null);
            mentorRoles.add(r);
            mentorRoles.add(m);

            Set<Role> adminRoles = new HashSet();
            Role a = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);
            adminRoles.add(a);


            

            // TODO faltan las relaciones. Actualizar una vez hechas las mismas para generar los objetos correctos.
            log.info("Starting to initialize sample data...");
            Faker faker = new Faker();

            /*
            System.out.println("------------ USUARIOS -------------");

            for (int i = 1; i < 11; i++) {
                Usuario user = new Usuario();
                user.setId(i);
                user.setNombre(faker.name().firstName());
                user.setApellido(faker.name().lastName());
                user.setUsername(user.getNombre() + user.getApellido());
                user.setEmail(user.getNombre() + user.getApellido() + "@gire.com");
                user.setPassword(encoder.encode(user.getNombre()+"123"));
                user.setPhone(faker.phoneNumber().cellPhone());
                user.setHasTeams(faker.random().nextBoolean());
                user.setRoles(userRoles);
                System.out.println(user);
                usuarioRepository.save(user);
            }
            */

            //USUARIOS DE PRUEBA
            //TEST USUARIO TIPO USER****************************************************************
            Usuario userUser = new Usuario();
            userUser.setId(1);
            userUser.setNombre("Nicolas");
            userUser.setApellido("Rivas");
            userUser.setUsername(userUser.getNombre() + userUser.getApellido());
            userUser.setEmail((userUser.getNombre() + "." + userUser.getApellido() + "@gire.com").toLowerCase());
            userUser.setPassword(encoder.encode(userUser.getNombre()+"123"));
            userUser.setPhone("123456789");
            userUser.setHasTeams(true);
            userUser.setRoles(userRoles);
            usuarioRepository.save(userUser);
            System.out.println(userUser.toString());
            //****************************************************************************************

            //TEST MENTORES BACK****************************************************************
            Usuario userMentor = new Usuario();
            userMentor.setId(2);
            userMentor.setNombre("Javier");
            userMentor.setApellido("Ottina");
            userMentor.setUsername(userMentor.getNombre() + userMentor.getApellido());
            userMentor.setEmail((userMentor.getNombre() + "." + userMentor.getApellido() + "@gire.com").toLowerCase());
            userMentor.setPassword(encoder.encode(userMentor.getNombre()+"123"));
            userMentor.setPhone("+54 11 1562-4513");
            userMentor.setHasTeams(true);
            userMentor.setMentorArea("BACKEND");
            userMentor.setRoles(mentorRoles);
            usuarioRepository.save(userMentor);
            System.out.println(userMentor.toString());

            Usuario userEmilioDubois = new Usuario();
            userEmilioDubois.setId(3);
            userEmilioDubois.setNombre("Emilio");
            userEmilioDubois.setApellido("Dubois");
            userEmilioDubois.setUsername(userEmilioDubois.getNombre() + userEmilioDubois.getApellido());
            userEmilioDubois.setEmail((userEmilioDubois.getNombre() + "." + userEmilioDubois.getApellido() + "@gire.com").toLowerCase());
            userEmilioDubois.setPassword(encoder.encode(userEmilioDubois.getNombre()+"123"));
            userEmilioDubois.setPhone("+54 11 5513-1234");
            userEmilioDubois.setHasTeams(true);
            userEmilioDubois.setMentorArea("BACKEND");
            userEmilioDubois.setRoles(mentorRoles);
            usuarioRepository.save(userEmilioDubois);
            System.out.println(userEmilioDubois.toString());

            //TEST MENTORES FRONT
            Usuario userCristianTeves = new Usuario();
            userCristianTeves.setId(4);
            userCristianTeves.setNombre("Cristian");
            userCristianTeves.setApellido("Teves");
            userCristianTeves.setUsername(userCristianTeves.getNombre() + userCristianTeves.getApellido());
            userCristianTeves.setEmail((userCristianTeves.getNombre() + "." + userCristianTeves.getApellido() + "@gire.com").toLowerCase());
            userCristianTeves.setPassword(encoder.encode(userCristianTeves.getNombre()+"123"));
            userCristianTeves.setPhone("+54 11 4163-1234");
            userCristianTeves.setHasTeams(true);
            userCristianTeves.setMentorArea("FRONTEND");
            userCristianTeves.setRoles(mentorRoles);
            usuarioRepository.save(userCristianTeves);
            System.out.println(userCristianTeves.toString());

            Usuario userCynthiaAvila = new Usuario();
            userCynthiaAvila.setId(5);
            userCynthiaAvila.setNombre("Cynthia");
            userCynthiaAvila.setApellido("Avila");
            userCynthiaAvila.setUsername(userCynthiaAvila.getNombre() + userCynthiaAvila.getApellido());
            userCynthiaAvila.setEmail((userCynthiaAvila.getNombre() + "." + userCynthiaAvila.getApellido() + "@gire.com").toLowerCase());
            userCynthiaAvila.setPassword(encoder.encode(userCynthiaAvila.getNombre()+"123"));
            userCynthiaAvila.setPhone("+54 11 6231-3134");
            userCynthiaAvila.setHasTeams(true);
            userCynthiaAvila.setMentorArea("FRONTEND");
            userCynthiaAvila.setRoles(mentorRoles);
            usuarioRepository.save(userCynthiaAvila);
            System.out.println(userCynthiaAvila.toString());

            Usuario userCynthiaRivero = new Usuario();
            userCynthiaRivero.setId(6);
            userCynthiaRivero.setNombre("Cynthia");
            userCynthiaRivero.setApellido("Rivero");
            userCynthiaRivero.setUsername(userCynthiaRivero.getNombre() + userCynthiaRivero.getApellido());
            userCynthiaRivero.setEmail((userCynthiaRivero.getNombre() + "." + userCynthiaRivero.getApellido() + "@gire.com").toLowerCase());
            userCynthiaRivero.setPassword(encoder.encode(userCynthiaRivero.getNombre()+"123"));
            userCynthiaRivero.setPhone("+54 11 1134-5314");
            userCynthiaRivero.setHasTeams(true);
            userCynthiaRivero.setMentorArea("FRONTEND");
            userCynthiaRivero.setRoles(mentorRoles);
            usuarioRepository.save(userCynthiaRivero);
            System.out.println(userCynthiaRivero.toString());

            //TEST USUARIO TIPO ADMIN****************************************************************
            Usuario userAdmin = new Usuario();
            userAdmin.setId(7);
            userAdmin.setNombre("Adrian");
            userAdmin.setApellido("Pierro");
            userAdmin.setUsername(userAdmin.getNombre() + userAdmin.getApellido());
            userAdmin.setEmail((userAdmin.getNombre() + "." + userAdmin.getApellido() + "@gire.com").toLowerCase());
            userAdmin.setPassword(encoder.encode(userAdmin.getNombre()+"123"));
            userAdmin.setPhone("+54 11 9123-1231");
            userAdmin.setHasTeams(true);
            userAdmin.setRoles(adminRoles);
            usuarioRepository.save(userAdmin);
            System.out.println(userAdmin.toString());
            //****************************************************************************************

            /*
            System.out.println("------------ SOLICITUDES -------------");

            for (int i = 1; i < 11; i++) {
                Solicitud solicitud = new Solicitud();
                solicitud.setId(i);
                solicitud.setTipo("UDEMY");
                solicitud.setDescripcion(faker.chuckNorris().fact());
                solicitud.setEstado("PENDIENTE-MENTOR");
                solicitud.setArea("BACKEND");
                solicitud.setTiempoSolicitado(i);
                solicitud.setUsuario(usuarioRepository.findById(1l));
                System.out.println(solicitud);
                solicitudRepository.save(solicitud);
            }
            */
            //SOLICITUDES DE PRUEBA
            Solicitud solicitud1 = new Solicitud();
            solicitud1.setId(1);
            solicitud1.setTipo("OTROS");
            solicitud1.setDescripcion("Queria realizar un curso sobre cookies");
            solicitud1.setArea("BACKEND");
            solicitud1.setEstado("PENDIENTE-MENTOR");
            solicitud1.setUsuario(userUser);
            solicitudRepository.save(solicitud1);
            System.out.println(solicitud1.toString());

            Solicitud solicitud2 = new Solicitud();
            solicitud2.setId(2);
            solicitud2.setTipo("ASESORAMIENTO");
            solicitud2.setDescripcion("Necesitaba asesoramiento sobre un curso que quiero hacer de JAVA");
            solicitud2.setArea("BACKEND");
            solicitud2.setEstado("PENDIENTE-MENTOR");
            solicitud2.setUsuario(userMentor);
            solicitudRepository.save(solicitud2);
            System.out.println(solicitud2.toString());


            System.out.println("------------ LICENCIAS -------------");
            for (int i = 1; i < 11; i++) {
                Licencia licencia = new Licencia();
                licencia.setId(i);
                licencia.setSerie(faker.bothify("????##?###???###"));
                licencia.setEstado("DISPONIBLE");
                licencia.setPlataforma("UDEMY");
                licencia.setSolicitudes(new ArrayList<>());
                System.out.println(licencia);
                licenciaRepository.save(licencia);
            }
            //LICENCIA DE PRUEBA
            Licencia licenciaPrueba= licenciaRepository.findById(1L);
            System.out.println("licenciaPrueba = " + licenciaPrueba);
            log.info("Finished with data initialization");

            for (int i = 11; i < 22; i++) {
                Licencia licenciaNueva = new Licencia();
                licenciaNueva.setId(20);
                licenciaNueva.setSerie(faker.bothify("????##?###???###"));
                licenciaNueva.setEstado("DISPONIBLE");
                licenciaNueva.setPlataforma("OTRA PLATAFORMA");
                licenciaNueva.setSolicitudes(new ArrayList<>());
                System.out.println(licenciaNueva);
                licenciaRepository.save(licenciaNueva);
            }

            //TEST USUARIOS TIPO USER
            Usuario usuarioRubio = new Usuario();
            usuarioRubio.setId(8);
            usuarioRubio.setNombre("Agustin");
            usuarioRubio.setApellido("Rubio");
            usuarioRubio.setUsername("AgustinRubio");
            usuarioRubio.setEmail((usuarioRubio.getNombre()+"."+usuarioRubio.getApellido()+"@gire.com").toLowerCase());
            usuarioRubio.setPassword(encoder.encode(usuarioRubio.getNombre()+"123"));
            usuarioRubio.setPhone("+54 11 1351-1234");
            usuarioRubio.setHasTeams(false);
            usuarioRubio.setRoles(userRoles);
            System.out.println(usuarioRubio.toString());
            usuarioRepository.save(usuarioRubio);

            Usuario userUserValenBara = new Usuario();
            userUserValenBara.setId(9);
            userUserValenBara.setNombre("Valentin");
            userUserValenBara.setApellido("Barallobre");
            userUserValenBara.setUsername("ValentinBarallobre");
            userUserValenBara.setEmail((userUserValenBara.getNombre()+"."+userUserValenBara.getApellido()+"@gire.com").toLowerCase());
            userUserValenBara.setPassword(encoder.encode(userUserValenBara.getNombre()+"123"));
            userUserValenBara.setPhone("+54 11 9131-4621");
            userUserValenBara.setHasTeams(true);
            userUserValenBara.setRoles(userRoles);
            System.out.println(userUserValenBara.toString());
            usuarioRepository.save(userUserValenBara);

            Usuario userUserFrancoJimenez = new Usuario();
            userUserFrancoJimenez.setId(10);
            userUserFrancoJimenez.setNombre("Franco");
            userUserFrancoJimenez.setApellido("Jimenez");
            userUserFrancoJimenez.setUsername("FrancoJimenez");
            userUserFrancoJimenez.setEmail((userUserFrancoJimenez.getNombre()+"."+userUserFrancoJimenez.getApellido()+"@gire.com").toLowerCase());
            userUserFrancoJimenez.setPassword(encoder.encode(userUserFrancoJimenez.getNombre()+"123"));
            userUserFrancoJimenez.setPhone("+54 11 8473-1613");
            userUserFrancoJimenez.setHasTeams(true);
            userUserFrancoJimenez.setRoles(userRoles);
            usuarioRepository.save(userUserFrancoJimenez);

            Usuario userUserMateoRubianes = new Usuario();
            userUserMateoRubianes.setId(11);
            userUserMateoRubianes.setNombre("Mateo");
            userUserMateoRubianes.setApellido("Rubianes");
            userUserMateoRubianes.setUsername("MateoRubianes");
            userUserMateoRubianes.setEmail((userUserMateoRubianes.getNombre()+"."+userUserMateoRubianes.getApellido()+"@gire.com").toLowerCase());
            userUserMateoRubianes.setPassword(encoder.encode(userUserMateoRubianes.getNombre()+"123"));
            userUserMateoRubianes.setPhone("+54 11 1639-8745");
            userUserMateoRubianes.setHasTeams(true);
            userUserMateoRubianes.setRoles(userRoles);
            usuarioRepository.save(userUserMateoRubianes);

            Usuario userUserMateoAmodio = new Usuario();
            userUserMateoAmodio.setId(12);
            userUserMateoAmodio.setNombre("Mateo");
            userUserMateoAmodio.setApellido("Amodio");
            userUserMateoAmodio.setUsername("MateoAmodio");
            userUserMateoAmodio.setEmail((userUserMateoAmodio.getNombre()+"."+userUserMateoAmodio.getApellido()+"@gire.com").toLowerCase());
            userUserMateoAmodio.setPassword(encoder.encode(userUserMateoAmodio.getNombre()+"123"));
            userUserMateoAmodio.setPhone("+54 11 9137-1357");
            userUserMateoAmodio.setHasTeams(true);
            userUserMateoAmodio.setRoles(userRoles);
            usuarioRepository.save(userUserMateoAmodio);


            //Solicitud RUBIO
            Solicitud solicitud3 = new Solicitud();
            solicitud3.setId(3);
            solicitud3.setTipo("UDEMY");
            solicitud3.setLink("https://www.udemy.com/course/aprende-javascript-sin-dolor/");
            solicitud3.setDescripcion("Queria realizar este curso");
            solicitud3.setArea("FRONTEND");
            solicitud3.setEstado("PENDIENTE-MENTOR");
            solicitud3.setUsuario(usuarioRubio);
            solicitudRepository.save(solicitud3);
            System.out.println(solicitud3.toString());

            //Solicitud BARA
            Solicitud solicitud4 = new Solicitud();
            solicitud4.setId(4);
            solicitud4.setTipo("OTRA PLATAFORMA");
            solicitud4.setLink("https://www.coderhouse.com/course/aprende-java/");
            solicitud4.setDescripcion("Necesito este curso sobre JAVA");
            solicitud4.setArea("BACKEND");
            solicitud4.setEstado("PENDIENTE-MENTOR");
            solicitud4.setUsuario(userUserValenBara);
            solicitudRepository.save(solicitud4);
            System.out.println(solicitud4.toString());

            //Solicitud Franji
            Solicitud solicitud5 = new Solicitud();
            solicitud5.setId(5);
            solicitud5.setTipo("UDEMY");
            solicitud5.setLink("https://www.udemy.com/course/curso-diseno-web-avanzado-html5-css3-js/");
            solicitud5.setDescripcion("Queria una licencia para este curso sobre css 3");
            solicitud5.setArea("FRONTEND");
            solicitud5.setEstado("PENDIENTE-MENTOR");
            solicitud5.setUsuario(userUserFrancoJimenez);
            solicitudRepository.save(solicitud5);
            System.out.println(solicitud5.toString());

            //Solicitud MateoAmodio
            Solicitud solicitud6 = new Solicitud();
            solicitud6.setId(6);
            solicitud6.setTipo("OTRA PLATAFORMA");
            solicitud6.setLink("https://www.coderhouse.com/course/security_desde_cero/");
            solicitud6.setDescripcion("Queria realizar este curso sobre security");
            solicitud6.setArea("BACKEND");
            solicitud6.setEstado("PENDIENTE-MENTOR");
            solicitud6.setUsuario(userUserMateoAmodio);
            solicitudRepository.save(solicitud6);
            System.out.println(solicitud6.toString());

            //Solicitud MateoRubia
            Solicitud solicitud7 = new Solicitud();
            solicitud7.setId(7);
            solicitud7.setTipo("OTRA PLATAFORMA");
            solicitud7.setLink("https://www.coderhouse.com/course/react-angular/");
            solicitud7.setDescripcion("Queria realizar este curso sobre react-angular");
            solicitud7.setArea("FRONTEND");
            solicitud7.setEstado("PENDIENTE-MENTOR");
            solicitud7.setUsuario(userUserMateoRubianes);
            solicitudRepository.save(solicitud7);
            System.out.println(solicitud7.toString());
        }
    }
}
