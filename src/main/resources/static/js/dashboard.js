document.addEventListener("DOMContentLoaded", function () {

    /* -----------------------------------------------
       Helper pour créer un graphique Chart.js
    ----------------------------------------------- */
    function createChart(ctx, type, labels, data, options = {}) {
        if (!ctx) return;
        new Chart(ctx, {
            type: type,
            data: {
                labels: labels,
                datasets: [{
                    label: options.label || "",
                    data: data,
                    backgroundColor: options.backgroundColor || [
                        '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'
                    ]
                }]
            },
            options: options.chartOptions || {}
        });
    }

    /* ------------------ Dashboard Citoyen ------------------ */
    const repartitionTypeChartEl = document.getElementById("repartitionTypeChart");
    const problemesParMoisChartEl = document.getElementById("problemesParMoisChart");

    if (repartitionTypeChartEl && problemesParMoisChartEl) {
        const typeLabels = JSON.parse(repartitionTypeChartEl.dataset.labels || "[]");
        const typeData = JSON.parse(repartitionTypeChartEl.dataset.data || "[]");

        createChart(repartitionTypeChartEl.getContext("2d"), "pie", typeLabels, typeData);

        const moisLabels = JSON.parse(problemesParMoisChartEl.dataset.labels || "[]");
        const moisData = JSON.parse(problemesParMoisChartEl.dataset.data || "[]");

        createChart(problemesParMoisChartEl.getContext("2d"), "bar", moisLabels, moisData, {
            label: "Problèmes par mois",
            backgroundColor: "#36A2EB"
        });
    }

    /* ------------------ Dashboard Administrateur ------------------ */
    const typeProblemeChartEl = document.getElementById("typeProblemeChart");
    const problemeMoisChartEl = document.getElementById("problemesParMoisChart");

    if (typeProblemeChartEl && problemeMoisChartEl) {
        const typeLabels = JSON.parse(typeProblemeChartEl.dataset.labels || "[]");
        const typeData = JSON.parse(typeProblemeChartEl.dataset.data || "[]");

        createChart(typeProblemeChartEl.getContext("2d"), "pie", typeLabels, typeData);

        const moisLabels = JSON.parse(problemeMoisChartEl.dataset.labels || "[]");
        const moisData = JSON.parse(problemeMoisChartEl.dataset.data || "[]");

        createChart(problemeMoisChartEl.getContext("2d"), "bar", moisLabels, moisData, {
            label: "Problèmes par mois",
            backgroundColor: "#36A2EB"
        });
    }

    /* ------------------ Dashboard SuperAdmin ------------------ */
    const superTypeChartEl = document.getElementById("typeProblemeChart");
    const superMoisChartEl = document.getElementById("problemesParMoisChart");
    const superMairieChartEl = document.getElementById("problemesParMairieChart");

    if (superTypeChartEl && superMoisChartEl && superMairieChartEl) {

        const typeLabels = JSON.parse(superTypeChartEl.dataset.labels || "[]");
        const typeData = JSON.parse(superTypeChartEl.dataset.data || "[]");
        createChart(superTypeChartEl.getContext("2d"), "pie", typeLabels, typeData);

        const moisLabels = JSON.parse(superMoisChartEl.dataset.labels || "[]");
        const moisData = JSON.parse(superMoisChartEl.dataset.data || "[]");
        createChart(superMoisChartEl.getContext("2d"), "bar", moisLabels, moisData, {
            label: "Problèmes par mois",
            backgroundColor: "#36A2EB"
        });

        const mairieLabels = JSON.parse(superMairieChartEl.dataset.labels || "[]");
        const mairieData = JSON.parse(superMairieChartEl.dataset.data || "[]");
        createChart(superMairieChartEl.getContext("2d"), "bar", mairieLabels, mairieData, {
            label: "Problèmes par mairie",
            backgroundColor: "#FF6384"
        });
    }
});