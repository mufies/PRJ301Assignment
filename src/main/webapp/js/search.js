let allFoods = [];

fetch("search")
    .then(res => res.json())
    .then(data => {
        allFoods = data;
    });

function removeVietnameseTones(str) {
    return str.normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .replace(/đ/g, "d")
        .replace(/Đ/g, "D");
}

function fetchResults() {
    const inputRaw = document.getElementById("foodInput").value.trim().toLowerCase();
    const input = removeVietnameseTones(inputRaw);
    const popup = document.getElementById("suggestionsPopup");

    popup.innerHTML = "";

    if (input === "") {
        popup.style.display = "none";
        return;
    }

    const matched = allFoods.filter(food =>
        removeVietnameseTones(food.name.toLowerCase()).includes(input)
    );

    if (matched.length === 0) {
        const noResult = document.createElement("div");
        noResult.className = "no-result";
        noResult.textContent = "Không tìm thấy món ăn phù hợp.";
        popup.appendChild(noResult);
        popup.style.display = "block";
        return;
    }

    matched.forEach(food => {
        const card = document.createElement("div");
        card.className = "result-card";

        const img = document.createElement("img");
        img.src = food.img;
        img.alt = food.name;

        const infoContainer = document.createElement("div");
        infoContainer.style.textAlign = "left";

        const name = document.createElement("div");
        name.textContent = food.name;
        name.style.fontWeight = "bold";
        name.style.fontSize = "16px";
        name.style.color = "#333";

        const price = document.createElement("div");
        price.textContent = "Giá: " + food.price + " ₫";
        price.style.color = "#28a745";
        price.style.fontSize = "14px";

        infoContainer.appendChild(name);
        infoContainer.appendChild(price);

        card.appendChild(img);
        card.appendChild(infoContainer);

        card.onclick = () => {
            addToCart(food.id.toString(), food.name, food.img, food.price.toFixed(0).toString());
        };

        popup.appendChild(card);
    });

    popup.style.display = "block";
}

document.addEventListener("click", function (e) {
    const popup = document.getElementById("suggestionsPopup");
    const input = document.getElementById("foodInput");
    if (!popup.contains(e.target) && e.target !== input) {
        popup.style.display = "none";
    }
});