<html>
    <head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<g:javascript src="jquery-ui/jquery.form.js" />
		<g:javascript library="flot" />
		<!--[if IE]><g:javascript library="excanvas" /><![endif]-->
		<script src="http://code.highcharts.com/highcharts.js"></script>
		<script src="http://code.highcharts.com/modules/exporting.js"></script>
    </head>
    <body>

	<script type="text/javascript">
		var chart;
		$(document).ready(function() {
			chart = new Highcharts.Chart({
				chart: {
					renderTo: 'container',
					plotBackgroundColor: 'rgb(247, 247, 247)',
					plotBorderWidth: null,
					plotShadow: false,
					backgroundColor: 'rgb(247, 247, 247)',
				},
				 title: {
					text: ''
				},
				credits: {
					  enabled: false,

     			},
				tooltip: {
					formatter: function() {
						return '<b>'+ this.point.name +'</b>: '+ this.percentage.toFixed(2) +' %';
					}
				},
				series: [{
					type: 'pie',
					name: 'Time spent by project',
					data: [
                            <g:each in="${timeSpentTop}" var="project">
                        {
                            name:'${project.projectName}',
                            y: <g:formatNumber number="${project.timePct}" format="#0.00" locale="en"/>,
                        },
                        </g:each>
					]
				}]
			});
		});

		var chart;
		$(document).ready(function() {

			var colors = Highcharts.getOptions().colors,
				categories = [<g:each in="${knowledge}" var="k">'${k.user}', </g:each>],
				data = [<g:each in="${knowledge}" var="k">${k.points}, </g:each>];

			function setChart(name, categories, data, color) {
				chart.xAxis[0].setCategories(categories);
				chart.series[0].remove();
				chart.addSeries({
					name: name,
					data: data,
					color: color || 'white'
				});
			}

			chart = new Highcharts.Chart({
				chart: {
					renderTo: 'container2',
					type: 'column',
					plotBackgroundColor: 'rgb(247, 247, 247)',
					backgroundColor: 'rgb(247, 247, 247)'
				},
				legend: {
				    enabled: false
				},
				title: {
					text: ''
				},
				xAxis: {
					categories: categories
				},
				yAxis: {
					title: {
						text: ''
					}
				},
				credits: {
					  enabled: false,
     			},
				tooltip: {
					formatter: function() {
					    return this.x +':<b>'+ this.y +'</b><br/>';
					}
				},
				series: [{
					data: data,
				}],
			});
		});

		var chart;
		$(document).ready(function() {
			chart = new Highcharts.Chart({
				chart: {
					renderTo: 'container3',
					type: 'column',
					plotBackgroundColor: 'rgb(247, 247, 247)',
					backgroundColor: 'rgb(247, 247, 247)'
				},
				title: {
					text: ''
				},
				xAxis: {
					categories: [<g:each in="${dates}" var="dateName">'${dateName}', </g:each>]
				},
				yAxis: {
					min: 0,
                    max: 10,
					title: {
						text: ''
					},
                    stackLabels:{
                        enabled:true,
                        style:{
                            fontWeight:'bold',
                            color:(Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                        }
                    }
				},
				tooltip: {
					formatter: function() {
                        return '<b>' + this.x + '</b><br/>' +
                                this.series.name + ': ' + this.y + '<br/>' +
                                'Total: ' + this.point.stackTotal;
					}
				},
				credits: {
					  enabled: false,
     			},
				plotOptions: {
					column: {
                        stacking:'normal',
                        dataLabels:{
                            enabled:true,
                            color:(Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                        },
						pointPadding: 0.2,
						borderWidth: 0
					}
				},
					series: [
                        <g:each in="${orderedEfforts}" var="efforts" status="status">
                        {
                            name: '${projects[status]}',
                            data:[<g:each in="${efforts}" var="timeSpent">${timeSpent > 0 ? timeSpent : 'null'}, </g:each> ]

                        },
                        </g:each>
				]
			});
		});

	</script>

    <div class="body">

		<div class="div-new-dashboard">

			<div class="div-new-dashboard-title">Dashboard</div>

			<div class="div-new-dashboard-row">
				<div class="div-new-dashboard-column">
					<div class="div-new-dashboard-column-title">New Projects</div>
					<div class="div-new-dashboard-column-content">
						<ul>
                            <g:each in="${newProjects}" var="project">
                                <li>${project.name} <g:formatDate date="${project.startDate}" format="dd-MMM" /></li>
                            </g:each>
                        </ul>
					</div>
					<div class="div-new-dashboard-column-title">New Birthdays</div>
					<div class="div-new-dashboard-column-content">
						<ul>
                            <g:each in="${birthdays}" var="user">
                                <li>${user.name} <g:formatDate date="${user.birthday}" format="dd-MMM" /></li>
                            </g:each>
                        </ul>
					</div>
				</div>
				<div class="div-new-dashboard-column div-margin">
					<div class="div-new-dashboard-column-title">Hours by Projects</div>
					<div id="container"></div>
				</div>
				<div class="div-new-dashboard-column">
					<div class="div-new-dashboard-column-title">My Efforts</div>
					<div id="container3"></div>
				</div>
			</div>

			<div class="div-new-dashboard-row">
				<div class="div-new-dashboard-column">
					<div class="div-new-dashboard-column-title">My Projects</div>
					<div class="div-new-dashboard-column-content2">
					   <ul>
                            <g:each in="${myProjects}" var="project">
                                <li>
                                    <a href="<g:createLink controller="dashboard" action="projectFollowUp" params="${ [dateStart: g.formatDate('date': startDate, type: 'date', locale: 'en'), dateEnd: g.formatDate('date': endDate, type: 'date', locale: 'en'), projectId: project.id] }"/>">
                                        ${project.name}
                                    </a>
                                </li>
                            </g:each>
                        </ul>
					</div>
				</div>
				<div class="div-new-dashboard-column div-margin">
					<div class="div-new-dashboard-column-title">Knowledge Ranking</div>
					<div id="container2"></div>
				</div>
				<div class="div-new-dashboard-column">
					<div class="div-new-dashboard-column-title">My Team Partners</div>
					<div class="div-new-dashboard-column-content2">
						<ul>
                            <g:each in="${myPartners}" var="partner">
                                <li>
                                    <a href="<g:createLink controller="user" action="showReports" id="${partner.id}"/>">${partner.name}</a>
                                </li>
                            </g:each>
                        </ul>
					</div>
				</div>
			</div>

		</div>

    </div>
	</body>
</html>
