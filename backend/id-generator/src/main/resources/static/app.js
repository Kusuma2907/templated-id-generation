const templateSelect =
    document.getElementById("template");

const citySelect =
    document.getElementById("city");

const countInput =
    document.getElementById("count");

const issueButton =
    document.getElementById("issueButton");

const results =
    document.getElementById("results");

const recent =
    document.getElementById("recent");

const error =
    document.getElementById("error");


async function loadTemplates() {

    const response =
        await fetch("/api/v1/config/templates");

    const templates =
        await response.json();

    templates.forEach(template => {

        const option =
            document.createElement("option");

        option.value = template.id;

        option.textContent =
            `${template.id} - ${template.name}`;

        templateSelect.appendChild(option);
    });
}


async function loadCities() {

    const response =
        await fetch("/api/v1/config/cities");

    const cities =
        await response.json();

    cities.forEach(city => {

        const option =
            document.createElement("option");

        option.value =
            city.cityCode;

        option.textContent =
            `${city.cityCode} - ${city.cityName}`;

        citySelect.appendChild(option);
    });
}


async function issueIds() {

    error.textContent = "";

    results.innerHTML = "";

    const request = {

        templateId:
            templateSelect.value,

        city:
            citySelect.value,

        count:
            Number(countInput.value)
    };

    const response =
        await fetch(
            "/api/v1/ids/bulk",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(request)
            }
        );

    const data =
        await response.json();

    if (!response.ok) {

        error.textContent =
            `${data.code}: ${data.message}`;

        return;
    }

    data.ids.forEach(id => {

        const li =
            document.createElement("li");

        li.textContent = id;

        results.appendChild(li);
    });

    loadRecent();
}


async function loadRecent() {

    const response =
        await fetch("/api/v1/ids/recent");

    const data =
        await response.json();

    recent.innerHTML = "";

    data.forEach(item => {

        const li =
            document.createElement("li");

        li.textContent =
            item.formattedId;

        recent.appendChild(li);
    });
}


issueButton.addEventListener(
    "click",
    issueIds
);


async function initialize() {

    await loadTemplates();

    await loadCities();

    await loadRecent();
}


initialize();