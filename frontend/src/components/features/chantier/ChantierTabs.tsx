import {
  Tabs,
  Tab,
} from "@mui/material";


interface ChantierTabsProps {

  value: number;

  onChange: (
    event: React.SyntheticEvent,
    value: number
  ) => void;

}



export default function ChantierTabs({
  value,
  onChange,

}: ChantierTabsProps) {


  return (

    <Tabs

      value={value}

      onChange={onChange}

    >

      <Tab label="Détails" />

      <Tab label="Tâches" />

      <Tab label="Avancement" />

      <Tab label="Documents" />

      <Tab label="Incidents" />

      <Tab label="Livraisons" />


    </Tabs>

  );

}