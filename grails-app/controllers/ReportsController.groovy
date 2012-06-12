import ar.com.fdvs.dj.domain.builders.FastReportBuilder
import ar.com.fdvs.dj.domain.DynamicReport
import ar.com.fdvs.dj.core.DynamicJasperHelper
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager

import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn
import ar.com.fdvs.dj.domain.builders.ColumnBuilder
import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.JasperPrint

import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.JRExporter
import net.sf.jasperreports.engine.JRExporterParameter
import ar.com.fdvs.dj.domain.DJCalculation

import ar.com.fdvs.dj.domain.constants.GroupLayout
import java.text.SimpleDateFormat
import org.json.JSONObject
import org.jfree.data.xy.XYSeriesCollection
import org.jfree.data.xy.XYSeries
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.JFreeChart
import org.jfree.chart.ChartFactory
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.ChartFrame
import org.jfree.chart.renderer.xy.XYSplineRenderer
import org.jfree.chart.ChartUtilities
import org.jfree.data.time.TimeSeriesCollection
import org.jfree.data.time.TimeSeries
import org.jfree.data.time.Day
import java.awt.Color
import org.jfree.ui.RectangleInsets
import org.jfree.chart.renderer.xy.XYItemRenderer
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.chart.axis.DateAxis
import net.sf.jasperreports.engine.export.JRXlsExporter
import net.sf.jasperreports.engine.export.JRXlsExporterParameter
import ar.com.fdvs.dj.core.layout.ListLayoutManager
import grails.converters.JSON
import java.text.Format
import org.apache.commons.lang.time.FastDateFormat
import org.compass.core.CompassQuery

class ReportsController extends BaseController {

    static allowedMethods = [vote: "POST"]

    def index = { redirect(action: "list", params: params) }

    DatabaseService databaseService
    def searchableService

    def beforeInterceptor = [action:this.&auth]

  def auth() {
       try {
        findLoggedUser()
        return true
       } catch (Exception e){
        redirect(controller:'login', action:'auth')
        return false
       }
  }

  def ajaxVote = {
    def user = findLoggedUser()
    def learning = Learning.get(params.learningId)
    JSONObject jsonResponse = new JSONObject()

    def vote = LearningVote.withCriteria {
      eq("user", user)
      eq("learning", learning)
    }

    if (vote){
      jsonResponse.put('message', g.message(code:"reports.vote.twice"))
      jsonResponse.put('points', learning.points)
    } else if (learning.user == user) {
      jsonResponse.put('message', g.message(code:"reports.vote.own.learning"))
      jsonResponse.put('points', learning.points)
    } else {
      if (learning.points != null){
        learning.points++
      } else {
        learning.points=1
      }
      learning.save(flush:true)

      LearningVote learningVote = new LearningVote()
      learningVote.user = user
      learningVote.date = new Date()
      learningVote.vote = 1
      learningVote.learning = learning
      learningVote.save(flush:true)

      ScoreManager.addPoints(user, "KNOWLEDGE","VOTE")
      ScoreManager.addPoints(learning.user, "KNOWLEDGE", "VOTERECEIVED")

      jsonResponse.put('points', learning.points)
      jsonResponse.put('message', g.message(code:"reports.vote.ok"))
    }
    render jsonResponse.toString()
  }

    def knowledge = {
        def user = findLoggedUser()
        def likeCondition = ""

        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        params.max = Math.min(params?.max?.toInteger() ?: 10, 100)
        params.offset = params?.offset?.toInteger() ?: 0
        params.sort = params?.sort ?: "date"
        params.order = params?.order ?: "desc"

        def learnings = []
        def highlights = []
        def total
        if (params.search) {
            def descriptionHighlighter = { highlighter, index, sr ->
                highlights[index] = highlighter.fragment('description')
            }
            def result = searchableService.search([offset: params.offset, max: params.max, withHighlighter: descriptionHighlighter, escape: true]) {
                alias('Learning')
                must(term('company', user.company))
                must(queryString(params.search) {
                    useOrDefaultOperator()
                    setDefaultSearchProperty('description')
                })
                sort(CompassQuery.SortImplicitType.SCORE)
            }
            learnings = result.results
            total = searchableService.countHits([escape: true]) {
                alias('Learning')
                must(term('company', user.company))
                must(term('description', params.search))
            }
        } else {
            learnings = Learning.createCriteria().list(
                    max: params.max,
                    offset: params.offset) {
                eq "company", user.company
                like("description", "%" + likeCondition + "%")
                order(params.sort, params.order)
            }
            total = learnings.totalCount
        }

        if (params.learningSaved)
            flash.message = "reports.knowledge.new.learning.saved"

        render(view: 'knowledge', model: [user: user, learnings: learnings, highlights: highlights, totalLearnings: total, search: params.search])
    }


    def newKnowledge = {
    def user = findLoggedUser()
    if(!g.message(code: "reports.knowledge.new.learning.empty").equals(flash.message))
        flash.message = ""
    render(view: 'newKnowledge', model: [user: user])
  }

  def list = {
      def user = findLoggedUser()
      def projects = Project.withCriteria(){
        eq("company", user.company)
        eq("active", Boolean.TRUE)
        order("name", "asc")

      }
      def roles = Role.findAllByCompany(user.company)
      def users = User.findAllByCompany(user.company)

      [projects: projects, roles: roles, users: users]

  }

  def ranking = {
    def user = findLoggedUser()
    def List rankingList = new ArrayList()
    def rankingTotal = 0
    String startDate, endDate
    Date dateStart, dateEnd

    // If there are dates, perform search, if not, just show datepickers
    if (params.dateStart != null && params.dateEnd != null){
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")

      dateStart = new Date(params.dateStart)
      startDate = sdf.format(dateStart)
      dateEnd = new Date(params.dateEnd)
      endDate = sdf.format(dateEnd)

      rankingList = databaseService.getReportRanking(startDate, endDate, user.company)
      rankingTotal = databaseService.getReportRankingTotalScore(startDate, endDate, user.company)

      if (rankingList?.empty)
        flash.message=  g.message(code:"reports.search.empty")

    }
    return [userInstance: user, ranking: rankingList, rankingTotal: rankingTotal, startDate: startDate, endDate: endDate, startDateValue:dateStart, endDateValue:dateEnd]
  }

  def showUserInfo = {
    def user = User.get(params.id)

    def loggedUser = findLoggedUser()

    if (user.company != loggedUser.company){
      params.clear()
      flash.message="CHEAAAAAAATER"
      redirect(action: "knowledge", params:params)
    }


    def assignments = user.listActiveAssignments()
    def totalUsers  = User.countByCompany(loggedUser.company)
    def userRanking = databaseService.getReportUserHistoricRanking(user)

    return [userInstance: user, availablesChatTime: TimeZoneUtil.getAvailablePromptTimes(), timeZones: TimeZoneUtil.getAvailableTimeZones(), locales: LocaleUtil.getAvailableLocales(), assignments: assignments, totalUsers: totalUsers, userRanking: userRanking]

  }

  def saveNewLearning = {

      def user = findLoggedUser()

      String learningContent = params.learning?.trim()

      if (!learningContent) {
        params.messageEmpty = true
        flash.message=  g.message(code:"reports.knowledge.new.learning.empty")
        redirect(action:"newKnowledge")
		return
      }

      Learning newLearning

      GregorianCalendar calendar = GregorianCalendar.getInstance();
      Date date = new Date(calendar.getTimeInMillis());

      newLearning = new Learning()
      newLearning.date = date
      newLearning.user = user
      newLearning.company = user.company
      newLearning.points = 0
      newLearning.description = learningContent
      newLearning.save(flush: true)

      ScoreManager.addPoints(user, "KNOWLEDGE","LEARNING")
      params.clear()
      params.learningSaved = true
      redirect(action: "knowledge", params:params)
    }

  def createReport = {
      def user = findLoggedUser()
      Date date
      String startDate, endDate, projectId, roleId, userId
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")

      if (params.startDate){

        date = new Date(params.startDate)
        startDate = sdf.format(date)
      }

      if (params.endDate){
        date = new Date(params.endDate)
        endDate = sdf.format(date)
      }

      if (params.projectId)
        projectId =params.projectId

      if (params.roleId)
        roleId = params.roleId

      if (params.userId)
        userId = params.userId

      def reportRows  = databaseService.getReport(startDate, endDate, projectId, roleId, userId, user.company)
      def List report = new ArrayList()


      reportRows.each {
        ReportItem ri = new ReportItem()
        ri.user = it.getAt("user")
        ri.date = it.getAt("date")
        ri.timeSpent = it.getAt("timeSpent")
        ri.project = it.getAt("project")
        ri.role = it.getAt("role")
        ri.assignmentStartDate = it.getAt("assignmentStartDate")
        ri.assignmentEndDate = it.getAt("assignmentEndDate")
        ri.comment = it.getAt("comment")
        report.add(ri)
      }

      FastReportBuilder drb = new FastReportBuilder();
      drb.setTitle("Hourly Report")

      drb.setLeftMargin(20)
         .setRightMargin(20)
         .setTopMargin(20)
         .setBottomMargin(20);

      drb.setDetailHeight(20)
          .setHeaderHeight(30);

      drb.setColumnsPerPage(1);

    AbstractColumn c1 = ColumnBuilder.getInstance().setColumnProperty("project", String.class.getName()).setTitle("Project").setWidth(40).build();
    AbstractColumn c2 = ColumnBuilder.getInstance().setColumnProperty("user", String.class.getName()).setTitle("User").setWidth(30).build();
    AbstractColumn c3 = ColumnBuilder.getInstance().setColumnProperty("role", String.class.getName()).setTitle("Role").setWidth(30).build();
    AbstractColumn c4 = ColumnBuilder.getInstance().setColumnProperty("date", Date.class.getName()).setTitle("Date").setWidth(30).build();
    AbstractColumn c5 = ColumnBuilder.getInstance().setColumnProperty("timeSpent", Integer.class.getName()).setTitle("Time Spent").setWidth(40).setFixedWidth(true).build();
    AbstractColumn c6 = ColumnBuilder.getInstance().setColumnProperty("comment", String.class.getName()).setTitle("Comment").setWidth(60).build();

    drb.addColumn(c1);
    drb.addColumn(c3);
    drb.addColumn(c2);
    drb.addColumn(c4);
    drb.addColumn(c5);
    drb.addColumn(c6);

    drb.setUseFullPageWidth(true);

    drb.addGroups(3);
    drb.addFooterVariable(1,5,DJCalculation.SUM, null);
    drb.addFooterVariable(2,5,DJCalculation.SUM, null);
    drb.addFooterVariable(3,5,DJCalculation.SUM, null);
    drb.setGroupLayout(1, GroupLayout.VALUE_IN_HEADER_WITH_HEADERS )
    drb.setPrintColumnNames(true)

    DynamicReport dr = drb.build();

    JRDataSource ds =  new JRBeanCollectionDataSource(report);

    File outfile = File.createTempFile("hourly_report", "hourly_report");
    String reportExt
    Boolean excelReport = Boolean.parseBoolean(params.excel);

    if (excelReport) {
        reportExt = "xls"
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ListLayoutManager(), ds);
        exportReportToXLS(jp,outfile.getAbsolutePath());
        response.contentType = 'application/vnd.ms-excel'
    } else {
        reportExt = "pdf"
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
        exportReportToPDF(jp,outfile.getAbsolutePath());
        response.contentType = 'application/pdf'
    }

    FileInputStream ins = new FileInputStream(outfile);
    int inData = ins.read();
    while (inData != -1) {
        response.outputStream.write(inData);
        inData = ins.read();
    }
    ins.close();

    String reportName = "reporte"
    response.setHeader("Content-disposition", "attachment; filename=" +reportName + "."+reportExt);
    response.outputStream.close()

    return false
  }


  def ajaxUpdateUsersAndRoles = {

      def user = findLoggedUser()
      List users = new ArrayList()
      List roles = new ArrayList()

      if (params.projectId != null && !"undefined".equals(params.projectId)){
        // If we have a project to filter, we filter available roles and users.
        roles = Role.executeQuery("select distinct ro from Role as ro, Assignment as ass where  ass.role = ro and ass.project = ? order by ro.name asc", [Project.get(params.projectId)])
        users = User.executeQuery("select distinct us from User as us, Assignment as ass where  ass.user = us and ass.project = ? order by us.name asc", [Project.get(params.projectId)])
      } else {
          roles = Role.withCriteria(){
            eq("company", user.company)
            order("name", "asc")
          }
          users = User.withCriteria(){
            eq("company", user.company)
            order("name", "asc")
          }
      }

      render(builder:'json') {

        usersData {
          users.each {
            usersData(
                    id: it.id,
                    name: it.name
            )
          }
        }
        rolesData {
          roles.each {
            rolesData(
                    id: it.id,
                    name: it.name
            )
          }
        }

      }
  }

  def ajaxUpdateUsers = {

      def user = findLoggedUser()
      List users = new ArrayList()

      if (params.roleId != null){

        // If we have a project to filter, we filter available roles and users.
        if (params.projectId == null){
          users = User.executeQuery("select distinct us from User as us, Assignment as ass where  ass.user = us and ass.role = ? order by us.name asc", [Role.get(params.roleId)])
        } else {
          users = User.executeQuery("select distinct us from User as us, Assignment as ass where  ass.user = us and ass.project = ? and ass.role = ? order by us.name asc", [Project.get(params.projectId), Role.get(params.roleId)])
        }

      } else {
          users = User.withCriteria(){
            eq("company", user.company)
            order("name", "asc")
          }
      }

      render(builder:'json') {
        usersData {
          users.each {
            usersData(
                    id: it.id,
                    name: it.name
            )
          }
        }
      }
  }

	private void exportReportToPDF(JasperPrint jp, String path) throws JRException, FileNotFoundException{
		JRExporter exporter = new JRPdfExporter();

		File outputFile = new File(path);
		File parentFile = outputFile.getParentFile();
		if (parentFile != null)
			parentFile.mkdirs();
		FileOutputStream fos = new FileOutputStream(outputFile);

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos);

		exporter.exportReport();
	}

    private void exportReportToXLS(JasperPrint jp, String path) throws JRException, FileNotFoundException{
		JRExporter exporter = new JRXlsExporter();

		File outputFile = new File(path);
		File parentFile = outputFile.getParentFile();
		if (parentFile != null)
			parentFile.mkdirs();
		FileOutputStream fos = new FileOutputStream(outputFile);

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos);

        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);

		exporter.exportReport();

	}

  /**
   *   Chart company's mood
   */
  def mood = {

    def user = findLoggedUser()
    def date = new Date()
    def userList = User.findAllByCompany(user.company)

    def month
    def year

    if (params.selectedMonth && params.selectedYear){
      month = Integer.parseInt(params.selectedMonth)-1
      year  = Integer.parseInt(params.selectedYear)-1900
    } else {
      month = date.month
      year  = date.year
    }

    // 'selectedUser' should be selected only if requester is PROJECT LEADER or COMPANY OWNER
    if (params.selectedUser){
      user = User.get(Integer.parseInt(params.selectedUser))
    }

    def years  = databaseService.findAvailableYears()
    def months = [1,2,3,4,5,6,7,8,9,10,11,12]

    SimpleDateFormat sdf = new SimpleDateFormat("yy_MM_dd__hh_mm_ss")
    String dateString = sdf.format(date)

    GregorianCalendar gc = new GregorianCalendar()
    gc.set(year, month, 1) // year month date

    def startDate = new Date()
    def endDate = new Date()

    startDate.month = month
    startDate.date = 1
    startDate.year = year

    endDate.month = month
    endDate.year  = year
    endDate.date  = gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)

    def userMood = UserMood.withCriteria {
      eq("user", user)
      lt("date", endDate)
      gt("date", startDate)
      eq("company", user.company)
      order('date', 'asc')
    }

    TimeSeriesCollection dataset = new TimeSeriesCollection();
    dataset.setDomainIsPointsInTime(true);

    TimeSeries s1 = new TimeSeries("${user.name}", Day.class);
    TimeSeries s2 = new TimeSeries("${user.company.name}", Day.class);

    userMood.each { UserMood um ->
      s1.addOrUpdate(new Day(um.date.date, um.date.month+1, um.date.year+1900), um.value)
    }

    HashMap<Integer,Integer> allMood = databaseService.getCompanyMood(user, startDate, endDate)

    Iterator ite = allMood.keySet().iterator();
    while (ite.hasNext()) {
        Integer companyDate = (Integer) ite.next();
        Double companyAverageValue = (Double) allMood.get(companyDate);
        s2.addOrUpdate(new Day(companyDate, month+1, year+1900), companyAverageValue )
    }

    dataset.addSeries(s1)
    dataset.addSeries(s2)

    int monthTitle= month +1
    int yearTitle = year +1900
    // Creating the chart
    JFreeChart chart = ChartFactory.createTimeSeriesChart(
      "${user.name} - ${monthTitle}/${yearTitle}", // title
      g.message(code:"reports.graphic.x"),        // x-axis label
      g.message(code:"reports.graphic.y"),        // y-axis label
      dataset,                // data
      true,                    // create legend?
      true,                    // generate tooltips?
      false                    // generate URLs?
    );// Setting the chart properties
    chart.setBackgroundPaint(Color.white)
    XYPlot plot = (XYPlot) chart.getPlot()
    plot.setBackgroundPaint(Color.lightGray)
    plot.setDomainGridlinePaint(Color.white)
    plot.setRangeGridlinePaint(Color.white)
    plot.axisOffset = new RectangleInsets(5.0, 5.0, 5.0, 5.0)
    plot.setDomainCrosshairVisible(true)
    plot.setRangeCrosshairVisible(true)

    XYItemRenderer r = plot.getRenderer();
    DateAxis axis = (DateAxis) plot.getDomainAxis();
    axis.setDateFormatOverride(new SimpleDateFormat("dd/MM"));

    File tempDir = (File) new File(servletContext.getRealPath("/"));

    File tempFile = File.createTempFile("${user.id}_${dateString}" , ".jpg", tempDir );
    session.setAttribute(tempFile.getAbsolutePath(), tempFile);
                                        tempFile.deleteOnExit()
    ChartUtilities.saveChartAsJPEG(tempFile, chart, 500, 300);

    render(view: 'mood', model: [imagePath: tempFile.name, monthList: months, yearList: years, usersList: userList])
  }

    def final COLORS = ['#A2EF00', '#00B945', '#FFEF00', '#88B32D', '#238B49', '#BFB630', '#699B00', '#00782D', '#A69C00', '#BBF73E', '#37DC74', '#FFF340', '#CBF76F', '#63DC90', '#FFF673']

    def final MAX_GANTT_ROWS = 20

    def usersGantt = {
        session['projectColors'] = [:]
        def today = new Date().onlyDate
        def startDate = today - 7
        def endDate = today + 28
        def billings = [0: 'Todos', 1: 'Billable', 2: 'No Billable']
        def modes = Mode.list()
        def skills = Skill.list()
        [start: 0, max: MAX_GANTT_ROWS, startDate: startDate, endDate: endDate, billings: billings, modes: modes, skills: skills, billing: 1]
    }

    def usersGanttData = { UserGanttFilter cmd ->
        def user = findLoggedUser()
        def today = new Date().onlyDate
        def startDate = cmd.startDate ?: today - 7
        def endDate = cmd.endDate ?: today + 28
        String sql = '''select a.id, a.startDate, a.endDate, p.name, u.name, a.description, u.id, r.name, p.id
from Assignment a join a.project p join a.user u join a.role r
where a.startDate <= ?
and a.endDate >= ?
and a.deleted = false
and a.active = true
and p.company = ? '''
        def sqlParams = [endDate, startDate, user.company]
        if (cmd.billing != 0) {
            sql += 'and p.billable = ? '
            sqlParams << (cmd.billing == 1)
        }
        if (cmd.mode) {
            sql += 'and ('
            cmd.mode.eachWithIndex { m, idx ->
                if (idx > 0) {
                    sql += 'or '
                }
                sql += 'p.mode.id = ? '
                sqlParams << m
            }
            sql += ') '
        }
        if (cmd.skill) {
            sql += 'and ('
            cmd.skill.eachWithIndex { m, idx ->
                if (idx > 0) {
                    sql += 'or '
                }
                sql += 'exists (from u.skills s where s.id = ?) '
                sqlParams << m
            }
            sql += ') '
        }
        sql += 'order by u.name, p.name'
        def results = Assignment.executeQuery(sql, sqlParams)
        def map = results.groupBy { it[4] }
        def list = []
        def colorIndex = 0
        def projectColors = session['projectColors']
        if (!projectColors) {
            projectColors = [:]
            session['projectColors'] = projectColors
        }
        map.each { k, v ->
            def record = new GanttGroup(id: v[0][6], name: k, series: [])
            v.each {
                def projectId = it[8]
                def projectColor = projectColors[projectId]
                if (!projectColor) {
                    projectColor = COLORS[colorIndex++ % COLORS.size()]
                    projectColors[projectId] = projectColor
                }
                def subrecord = new GanttSubgroup(name: "${it[3]} - ${it[7]}", start: [it[1], startDate].max(), end: [it[2], endDate].min(), color: projectColor)
                record.series << subrecord
            }
            list << record
        }
        if (!list) {
            return list as JSON
        }
        def start = params.start ? params.start as int : 0
        if (start < 0) {
            start = 0
        }
        def end = Math.min(start + MAX_GANTT_ROWS, list.size()) - 1
        render list[start..end] as JSON
    }

}

class GanttGroup {
    Long id
    String name
    List series
}

class GanttSubgroup {
    String name
    Date start
    Date end
    String color
}

class UserGanttFilter {

    int start
    int max
    Date startDate
    Date endDate
    long billing
    long[] mode
    long[] skill
}
