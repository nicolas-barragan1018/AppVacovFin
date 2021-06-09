package co.edu.unipiloto.appvacov;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class UsersAdapter extends BaseAdapter implements Serializable {

    protected Context activity;
    protected ArrayList<Cita> items;
    private int p;
    private int layout;
    private  Cita c;

    public UsersAdapter(Context activity, int layout, ArrayList<Cita> items) {
        this.activity = activity;
        this.items = items;
        this.layout = layout;
    }

    public void setCita (int i, Cita c){ items.set(i,c); }

    @Override
    public int getCount() { return items.size(); }

    @Override
    public Object getItem(int position) { return items.get(position); }

    @Override
    public long getItemId(int position) { return 0;}

    @Override
    public View getView(int position, @NonNull View convertView,@NonNull ViewGroup parent) {

        View vi = convertView;
        p=position;

            LayoutInflater inflater = LayoutInflater.from(this.activity);
            vi = inflater.inflate(R.layout.item_cita, null);

            Cita item = items.get(position);
            TextView nombrePAC = (TextView) vi.findViewById(R.id.nombrePaciente);
            TextView cedulaPAC = (TextView) vi.findViewById(R.id.numeroCedula);
            TextView epsPAC = (TextView) vi.findViewById(R.id.nombrecitaEPS);
            TextView habilitadoPAC = (TextView) vi.findViewById(R.id.pacienteHabilitado);
            TextView vacunaAplicada = (TextView) vi.findViewById(R.id.vacunaAplicada);
            TextView horaCita = (TextView) vi.findViewById(R.id.horaCita);

            nombrePAC.setText(item.getNombrePaciente());
            cedulaPAC.setText(item.getDocumentoPaciente());
            epsPAC.setText(item.getEps());
            habilitadoPAC.setText(item.isRealizada() ? "Vacunado" : "Vacunar");
            habilitadoPAC.setTextColor(item.isRealizada() ? Color.GREEN : Color.RED);
            vacunaAplicada.setText(item.getNombreVacuna());
            horaCita.setText(item.getHora());

        return vi;
    }
}
