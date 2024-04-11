import React from 'react'
import ReactDOM from 'react-dom/client'
import InventoryGroup from './components/inventory-group'

export function onContentLoad() {
    const groups = document.getElementById("inventory-panel")
        .querySelectorAll("[data-component='inventory-group']");
    groups.forEach(group => {
        const obj = JSON.parse(group.getAttribute("data-object"));
        group = ReactDOM.createRoot(group);
        group.render(<InventoryGroup object={obj}/>);
    });
}

