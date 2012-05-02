import groovy.sql.Sql
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException

/**
 * @author Alejandro Gomez (alejandro.gomez@fdvsolutions.com)
 * Date: Aug 5, 2009
 * Time: 7:40:33 PM
 */
class Migrator {

    def tagsPerProject = [
            'Accendra - Comafi': ['accendra', 'comafi', ''],
            'Accendra - OSG (2008)': ['accendra', 'osg', ''],
            'Accendra - OSG (2009)': ['accendra', 'osg', ''],
            'Apernet - Club Personal': ['apernet', 'clubpersonal', ''],
            'Apernet - Invest Alliance - Organizer': ['apernet', 'invest', 'organizer'],
            'Apernet - Invest Alliance - Research Process': ['apernet', 'invest', 'research.process'],
            'Apernet - Invest Alliance - Third Party': ['apernet', 'invest', 'third.party'],
            'Apernet - Invest Alliance - Watch List': ['apernet', 'invest', 'watch.list'],
            'Apernet - InvestAlliance - Research Process FE': ['apernet', 'invest', 'research.process.fe'],
            'Biblioteca - Universidad 3 de Febrero': ['u3f', 'biblioteca', ''],
            'Corkboard.it': ['cockboard.it', '', ''],
            'Creative House - AA2000 Marruecos': ['creative.house', 'aa2000.marruecos', ''],
            'Creative House - Aeropuertos 2000': ['creative.house', 'aa2000', ''],
            'Creative House - Carmen Parking': ['creative.house', 'carmen.parking', ''],
            'Danone - Mantenimiento SMS': ['danone', 'sms', 'mantenimiento'],
            'Danone - Shift Management System': ['danone', 'sms', ''],
            'Danone - Sistema de Turnos 2.2': ['danone', 'turnos-2.2', ''],
            'Danone - Turnos': ['danone', 'turnos', ''],
            'Danone - Turnos - Extensión Multialmacén': ['danone', 'turnos', 'multi.almacen'],
            'Danone - Turnos 2.x - Manteniemiento': ['danone', 'turnos', 'mantenimiento'],
            'DJ - 2009': ['dj', '', ''],
            'DynamicJasper': ['dj', '', ''],
            'Everis - Capacitación WS': ['everis', 'capacitacion', ''],
            'FDV - Administración': ['fdv', 'administracion', ''],
            'FDV - Administración 2009': ['fdv', 'administracion.2009', ''],
            'FDV - Capacitacion Comercio Exterior': ['fdv', '', ''],
            'FDV - Commons': ['fdv', 'commons', ''],
            'FDV - Commons-web': ['fdv', 'commons.web', ''],
            'FDV - Dashboard': ['fdv', 'dashboard', ''],
            'FDV - Infraestructura': ['fdv', 'infraestructura', ''],
            'FDV - Institucional': ['fdv', 'institucional', ''],
            'FDV - Invoice': ['fdv', 'invoice', ''],
            'FDV - Template': ['fdv', 'template', ''],
            'FDV - Tero': ['fdv', 'tero', ''],
            'FDV - Timesheet': ['fdv', 'timesheet', ''],
            'FDV - Ventas 2008': ['fdv', 'ventas', ''],
            'FDV - Ventas 2009': ['fdv', 'ventas', ''],
            'Garbarino - STS': ['garbarino', 'sts', ''],
            'Garbarino - STS - Garantía': ['garbarino', 'sts', 'garantia'],
            'Gemalto 2008': ['gemalto', '', ''],
            'Gerencia de Servicios 2009': ['gcia.servicios', '', ''],
            'GS1 - Show Room': ['gs1', 'showroom', ''],
            'IPS - Reconciliaciones': ['ips', 'reconciliaciones', ''],
            'Manejo de Cuentas y Proyectos': ['ctas.proyectos', '', ''],
            'Mercado Vial': ['mercado.vial', '', ''],
            'Merck - Expensys': ['merck', 'expensys', ''],
            'Merck - Expensys v3.0': ['merck', 'expensys3', ''],
            'Merck - Mantenimiento': ['merck', 'mantenimiento', ''],
            'Merk - Mantenimiento': ['merck', 'mantenimiento', ''],
            'Ocio creativo': ['ocio', '', ''],
            'PDM - Balanza': ['pdm', 'balanza', ''],
            'PDM - Cotizaciones X Web': ['pdm', 'cxw', ''],
            'PDM - Interfaces': ['pdm', 'interfaces', ''],
            'PDM - RFID': ['pdm', 'rfid', ''],
            'Reunions, Propuestas y Ventas': ['reuniones', 'propuestas', 'ventas'],
            'RMyA - SUACI': ['rmya', 'suaci', ''],
            'Sutec - Tránsito Web': ['sutec', 'transito', ''],
            'Toyota - Pruis': ['toyota.prius', '', ''],
            'Trabajo REAL': ['trabajo.real', '', ''],
            'Tradecom - FMS': ['tradecom', 'fms', ''],
            'Tradecom - Municipalidad': ['tradecom', 'municipalidad', ''],
            '·········': ['x', '', '']
    ]

    def migrate() {
        def sql = Sql.newInstance('jdbc:mysql://192.168.1.100:3306/timesheet', 'timesheet', 'timesheet', 'com.mysql.jdbc.Driver')
        sql.eachRow('select razon_social from empresa', { empresa ->
            Company company = new Company(name: empresa.razon_social)
            company.save(flush: true)
        })
        int c = sql.firstRow('select count(1) as c from empresa').c
        assert c == Company.count()
        sql.eachRow('select nombre from rol', { rol ->
            Company.list().each { company ->
                Role role = new Role(name: rol.nombre, company: company)
                role.save(flush: true)
            }
        })
        c = sql.firstRow('select count(1) as c from rol').c
        assert c * Company.count() == Role.count()
        Permission userRole = Permission.findByName(Permission.ROLE_USER)
        sql.eachRow('select jabber_account, login, nombre, password, momento_consulta, enabled, locale, momento_consulta_local, time_zone, razon_social from persona p, empresa e where p.empresa_id = e.id', { persona ->
            User user = new User(password: persona.password, name: persona.nombre,
                    account: persona.jabber_account, chatTime: persona.momento_consulta, localChatTime: persona.momento_consulta_local, humour: 'sweet',
                    timeZone: persona.time_zone, locale: persona.locale, enabled: persona.enabled, company: Company.findByName(persona.razon_social),
                    permissions: [userRole]
            )
            user.save(flush: true)
            userRole.addToUsers(user)
        })
        c = sql.firstRow('select count(1) as c from persona').c
        assert c == User.count()
        sql.eachRow('select razon_social, nombre, inicio, fin from proyecto p, periodo q, empresa e where p.periodo_afectado_id = q.id and p.empresa_id = e.id', { proyecto ->
            Project project = new Project(name: proyecto.nombre, startDate: proyecto.inicio, endDate: proyecto.fin, company: Company.findByName(proyecto.razon_social))
            project.save(flush: true)
        });
        c = sql.firstRow('select count(1) as c from proyecto').c
        assert c == Project.count()
        createTags()
        Project.list().each { project ->
            tagsPerProject[(project.name)].each { tagName ->
                if (tagName) {
                    Tag tag = Tag.findByNameAndCompany(tagName, project.company)
                    project.addToTags(tag)
                }
                project.save(flush: true)
            }
        }
        sql.eachRow('select p.nombre as proyecto, q.inicio as inicio, q.fin as fin, r.nombre as rol, s.jabber_account as login from asignacion a, proyecto p, periodo q, rol r, persona s where a.proyecto_id = p.id and a.rol_id = r.id and a.periodo_afectado_id = q.id and a.persona_id = s.id', { asignacion ->
            Project project = Project.findByName(asignacion.proyecto)
            Assignment assignment = new Assignment(project: project, user: User.findByAccount(asignacion.login), role: Role.findByNameAndCompany(asignacion.rol, project.company), startDate: asignacion.inicio, endDate: asignacion.fin)
            assignment.save(flush: true)
        });
        c = sql.firstRow('select count(1) as c from asignacion').c
        assert c == Assignment.count()
        sql.eachRow('select e.descripcion as descripcion, e.duracion_en_horas as horas, p.jabber_account as login, q.nombre as proyecto, e.fecha as fecha from esfuerzo e, asignacion a, persona p, proyecto q where e.asignacion_id = a.id and a.persona_id = p.id and a.proyecto_id = q.id', { esfuerzo ->
            Company company = Project.findByName(esfuerzo.proyecto).company
            def tags = []
            tagsPerProject[(esfuerzo.proyecto)].each { tagName ->
                if (tagName) {
                    tags << Tag.findByNameAndCompany(tagName, company)
                }
            }
            Effort effort = new Effort(timeSpent: esfuerzo.horas, date: esfuerzo.fecha, comment: esfuerzo.descripcion, tags: tags, user: User.findByAccount(esfuerzo.login))
            try {
                effort.save(flush: true)
            } catch (DataIntegrityViolationException ex) {
                println ex
            }
        });
        c = sql.firstRow('select count(1) as c from esfuerzo').c
        assert c == Effort.count()
    }

    def createTags() {
        def clientCategory = TagCategory.findByName('client')
        def projectCategory = TagCategory.findByName('project')
        def taskCategory = TagCategory.findByName('task')
        tagsPerProject.each { p, ts ->
            Company company = Project.findByName(p).company
            ts.eachWithIndex { t, idx ->
                Tag tag = Tag.findByNameAndCompany(t, company)
                if (!tag) {
                    tag = new Tag(name: t, category: (idx == 0 ? clientCategory : (idx == 1 ? projectCategory : taskCategory)), company: company)
                    tag.save(flush: true)
                }
            }
        }
    }
}
