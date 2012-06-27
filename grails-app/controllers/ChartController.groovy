import jofc2.model.elements.BarChart
import jofc2.model.elements.BarChart.Bar
import jofc2.model.Chart
import jofc2.model.axis.XAxis
import jofc2.model.elements.StackedBarChart
import jofc2.model.elements.StackedBarChart.StackValue
import jofc2.model.elements.PieChart.Slice
import jofc2.model.elements.PieChart
import jofc2.model.axis.YAxis
import org.springframework.security.context.SecurityContextHolder

class ChartController extends BaseController {

    def static final COLORS = ['#70446E', '#924E4E', '#B67EA0', '#ED75AA', '#CB427E', '#F7E774', '#F47B4A', '#973292', '#EDA7DD', '#C0239C']

    def index = { FilterCommand filter ->
        filter.fromDate = filter.fromDate ?: new Date() - 7
        filter.toDate = filter.toDate ?: new Date()
        render(view: 'index', model: [filter: filter])
    }

    def chartData = { FilterCommand filter ->
        filter.tags = filter.tags.collect { tag ->
            tag
        }
        def query = 'select distinct e from Effort as e '
        query += 'join e.user as u '
        if (filter.tags.class.isArray()) {
            filter.tags.eachWithIndex { t, idx ->
                query += "left join e.tags as t$idx "
            }
        } else if (filter.tags) {
            query += "left join e.tags as t0 "
        }
        query += 'where 1 = 1 '
        query += 'and u.company = :c '
        query += filter.fromDate ? 'and e.date >= :df ' : ''
        query += filter.toDate ? 'and e.date < :dt ' : ''
        query += filter.users ? 'and u.account = :u ' : ''
        if (filter.tags.class.isArray()) {
            filter.tags.eachWithIndex { t, idx ->
                if (t) {
                    query += "and t${idx}.name = :t$idx "
                }
            }
        } else if (filter.tags) {
            query += "and t0.name = :t0 "
        }
        query += 'order by e.date asc'
        def qparams = [:]
        qparams.c = findLoggedUser().company
        if (filter.fromDate) {
            qparams.df = filter.fromDate
        }
        if (filter.toDate) {
            qparams.dt = filter.toDate + 1
        }
        if (filter.users) {
            qparams.u = filter.users
        }
        if (filter.tags.class.isArray()) {
            filter.tags.eachWithIndex { t, idx ->
                if (t) {
                    qparams["t$idx"] = t
                }
            }
        } else if (filter.tags) {
            qparams["t0"] = filter.tags
        }
        def efforts = Effort.executeQuery(query, qparams)
        if (!efforts) {
            render new Chart('No data found')
            return
        }
        if (efforts.size() > 50) {
            render new Chart('Too many data found')
            return
        }
        def groups = efforts.tags.unique()
        def keys = []
        groups.eachWithIndex { group, idx ->
            keys << new StackedBarChart.Key(COLORS[idx % COLORS.size()], group.join(','), 13)
        }
        def bars = []
        def labels = []
        def prevMonth = -1
        def sbc = new StackedBarChart()
        def firstDate = efforts.date.min()
        def lastDate = efforts.date.max()
        def maxByDate = 0
        (firstDate..lastDate).each { date ->
            def values = []
            def totalByDate = 0
            groups.eachWithIndex { group, idx ->
                def timeSpent = efforts.find { it.tags == group && it.date.clearTime() == date}?.timeSpent ?: 0
                if (timeSpent) {
                    values << new StackValue(timeSpent, COLORS[idx % COLORS.size()])
                }
                totalByDate += timeSpent
            }
            if (totalByDate > maxByDate) {
                maxByDate = totalByDate
            }
            sbc.newStack().addStackValues(values);
        }
        sbc.addKeys(keys as StackedBarChart.Key[])
        def c = new Chart(new Date().toString()).setXAxis(new XAxis().setLabels((firstDate..lastDate)*.format('dd/MM/yyyy').unique()))
        c.setYAxis(new YAxis().setMax(maxByDate))
        c.addElements(sbc);
        render c;
    }

    def byUser = { ByUserFilterCommand filter ->
        filter.fromDate = filter.fromDate ?: new Date() - 7
        filter.toDate = filter.toDate ?: new Date()
        render(view: 'byUser', model: [filter: filter])
    }

    def byUserChartData = { ByUserFilterCommand filter ->
        if (!filter.user || !checkCompany(filter.user)) {
            render new Chart('No data found')
            return
        }
        def query = 'select distinct e from Effort as e '
        query += 'join e.user as u '
        filter.tags.eachWithIndex { t, idx ->
            query += "left join e.tags as t$idx "
        }
        query += 'where 1 = 1 '
        query += filter.fromDate ? 'and e.date >= :fd ' : ''
        query += filter.toDate ? 'and e.date < :td ' : ''
        query += 'and u.account = :u '
        filter.tags.eachWithIndex { t, idx ->
            if (t) {
                query += "and t${idx}.name = :t$idx "
            }
        }
        query += 'order by e.date asc'
        def qparams = [:]
        if (filter.fromDate) {
            qparams.fd = filter.fromDate
        }
        if (filter.toDate) {
            qparams.td = filter.toDate + 1
        }
        qparams.u = filter.user
        filter.tags.eachWithIndex { t, idx ->
            if (t) {
                qparams["t$idx"] = t
            }
        }
        def efforts = Effort.executeQuery(query, qparams)
        if (!efforts) {
            render new Chart('No data found')
            return
        }
        if (efforts.size() > 50) {
            render new Chart('Too many data found')
            return
        }
        def groups = efforts.tags.unique()
        def keys = []
        groups.eachWithIndex { group, idx ->
            keys << new StackedBarChart.Key(COLORS[idx % COLORS.size()], group.join(','), 13)
        }
        def bars = []
        def labels = []
        def sbc = new StackedBarChart()
        def firstDate = efforts.date.min().clearTime()
        def lastDate = efforts.date.max().clearTime()
        (firstDate..lastDate).each { date ->
            def values = []
            groups.eachWithIndex { group, idx ->
                def timeSpent = efforts.find { it.tags == group && it.date.clearTime() == date}?.timeSpent ?: 0
                if (timeSpent) {
                    values << new StackValue(timeSpent, COLORS[idx % COLORS.size()])
                }
            }
            sbc.newStack().addStackValues(values);
        }
        sbc.addKeys(keys as StackedBarChart.Key[])
        def c = new Chart(new Date().toString()).setXAxis(new XAxis().setLabels((firstDate..lastDate)*.format('dd/MM/yyyy').unique()));
        c.addElements(sbc);
        render c;
    }

    def totalByUserChartData = { ByUserFilterCommand filter ->
        if (!filter.user || !checkCompany(filter.user)) {
            render new Chart('No data found')
            return
        }
        def query = 'select distinct e from Effort as e '
        query += 'join e.user as u '
        filter.tags.eachWithIndex { t, idx ->
            query += "left join e.tags as t$idx "
        }
        query += 'where 1 = 1 '
        query += filter.fromDate ? 'and e.date >= :fd ' : ''
        query += filter.toDate ? 'and e.date < :td ' : ''
        query += 'and u.account = :u '
        filter.tags.eachWithIndex { t, idx ->
            if (t) {
                query += "and t${idx}.name = :t$idx "
            }
        }
        query += 'order by e.date asc'
        def qparams = [:]
        if (filter.fromDate) {
            qparams.fd = filter.fromDate
        }
        if (filter.toDate) {
            qparams.td = filter.toDate
        }
        qparams.u = filter.user
        filter.tags.eachWithIndex { t, idx ->
            if (t) {
                qparams["t$idx"] = t
            }
        }
        def efforts = Effort.executeQuery(query, qparams)
        if (!efforts) {
            render new Chart('No data found')
            return
        }
        if (efforts.size() > 50) {
            render new Chart('Too many data found')
            return
        }
        def slices = []
        def groups = efforts.groupBy { Effort effort -> effort.tags }
        groups.each { tags, groupedEfforts ->
            Slice slice = new Slice(groupedEfforts.sum {Effort effort -> effort.timeSpent}, tags.join(', '))
            //slice.tip = "#val#hs of #total#hs<br>#percent# of 100%<br>${tags.join(', ')}"
            slice.tip = "#val#hs for ${tags.join(', ')}"
            slices << slice
        }
        Chart c = new Chart("Consumos", '{font-size: 18px;color #d01f3c}')
            .addElements(new PieChart()
                .addSlices(slices)
                .setAnimate(true)
                .setStartAngle(35)
                .setBorder(2)
                .setAlpha(0.6f)
                .setColours(COLORS)
                .setTooltip("#val#hs of #total#hs<br>#percent# of 100%"))
            .setBackgroundColour('#ffffff')
        render c;
    }

    def byDate = { ByDateFilterCommand filter ->
        filter.date = filter.date ?: new Date() - 1
        render(view: 'byDate', model: [filter: filter])
    }

    def byDateChartData = { ByDateFilterCommand filter ->
        def query = 'select distinct e from Effort as e '
        query += 'join e.user as u '
        query += 'where 1 = 1 '
        query += 'and e.date >= :date '
        query += 'and e.date < :nextDate '
        query += 'and u.company = :company'
        Map qparams = [
                date: filter.date,
                nextDate: filter.date + 1,
                company: findLoggedUser().company
        ]
        List efforts = Effort.executeQuery(query, qparams)
        if (!efforts) {
            render new Chart('No data found')
            return
        }
        def groups = new ArrayList(efforts.groupBy { Effort effort -> effort.tags }.keySet())
        def keys = []
        groups.eachWithIndex { group, idx ->
            keys << new StackedBarChart.Key(COLORS[idx % COLORS.size()], group.join(','), 13)
        }
        def bars = []
        def labels = []
        def prevMonth = -1
        def sbc = new StackedBarChart()
        def users = efforts*.user.unique()
        users.each { User user ->
            def values = []
            groups.eachWithIndex { group, idx ->
                def timeSpent = efforts.find { it.tags == group && it.user == user}?.timeSpent ?: 0
                if (timeSpent) {
                    values << new StackValue(timeSpent, COLORS[idx % COLORS.size()])
                }
            }
            sbc.newStack().addStackValues(values);
        }
        sbc.addKeys(keys as StackedBarChart.Key[])
        render new Chart(new Date().toString()).setXAxis(new XAxis().setLabels(users*.name)).addElements(sbc)
    }

    def totalByDateChartData = { ByDateFilterCommand filter ->
        def query = 'select distinct e from Effort as e '
        query += 'join e.user as u '
        query += 'where 1 = 1 '
        query += 'and e.date >= :date '
        query += 'and e.date < :nextDate '
        query += 'and u.company = :company'
        Map qparams = [
                date: filter.date,
                nextDate: filter.date + 1,
                company: findLoggedUser().company
        ]
        List efforts = Effort.executeQuery(query, qparams)
        if (!efforts) {
            render new Chart('No data found')
            return
        }
        def slices = []
        def groups = efforts.groupBy { Effort effort -> effort.tags }
        groups.each { tags, groupedEfforts ->
            slices << new Slice(groupedEfforts.sum { Effort effort -> effort.timeSpent}, tags.join(','))
        }
        Chart c = new Chart("Consumos", '{font-size: 18px;color #d01f3c}')
            .addElements(new PieChart()
                .addSlices(slices)
                .setAnimate(true)
                .setStartAngle(35)
                .setBorder(2)
                .setAlpha(0.6f)
                .setColours(COLORS)
                .setTooltip("#val#hs of #total#hs<br>#percent# of 100%"))
            .setBackgroundColour('#ffffff')
        render c;
    }
}

class FilterCommand {

    Date fromDate
    Date toDate
    String users
    String[] tags
}

class ByUserFilterCommand {

    Date fromDate
    Date toDate
    String user
    String[] tags

    def toParamsMap() {
        return [
            fromDate: 'struct',
            fromDate_day: fromDate?.date ?: '',
            fromDate_month: fromDate?.month + 1 ?: '',
            fromDate_year: fromDate?.year + 1900 ?: '',
            toDate: 'struct',
            toDate_day: toDate?.date ?: '',
            toDate_month: toDate?.month + 1 ?: '',
            toDate_year: toDate?.year + 1900 ?: '',
            user: user ?: '',
            tags: tags ?: ''
        ]
    }
}

class ByDateFilterCommand {

    Date date

    def toParamsMap() {
        return [
            date: 'struct',
            date_day: date?.date ?: '',
            date_month: date?.month + 1 ?: '',
            date_year: date?.year + 1900 ?: ''
        ]
    }
}
